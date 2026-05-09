package sk.tuke.gamestudio.server.webservice;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.entity.User;
import sk.tuke.gamestudio.server.dto.GameStateDto;
import sk.tuke.gamestudio.server.dto.MoveDto;
import sk.tuke.gamestudio.server.dto.NewGameRequestDto;
import sk.tuke.gamestudio.server.service.GameSession;
import sk.tuke.gamestudio.server.service.JigsawSudokuGameService;
import sk.tuke.gamestudio.server.service.ScoreCalculator;
import sk.tuke.gamestudio.server.service.auth.AuthCookieService;
import sk.tuke.gamestudio.server.service.auth.JwtService;
import sk.tuke.gamestudio.service.score.ScoreService;
import sk.tuke.gamestudio.service.user.UserService;

import java.util.Date;

@RestController
@RequestMapping("/api/engine")
public class JigsawSudokuRest {

  private final JigsawSudokuGameService gameService;
  private final AuthCookieService authCookieService;
  private final JwtService jwtService;
  private final UserService userService;
  private final ScoreCalculator scoreCalculator;
  private final ScoreService scoreService;

  public JigsawSudokuRest(
          JigsawSudokuGameService gameService,
          AuthCookieService authCookieService,
          JwtService jwtService,
          UserService userService,
          ScoreCalculator scoreCalculator,
          ScoreService scoreService
  ) {
    this.gameService = gameService;
    this.authCookieService = authCookieService;
    this.jwtService = jwtService;
    this.userService = userService;
    this.scoreCalculator = scoreCalculator;
    this.scoreService = scoreService;
  }

  @PostMapping("/games")
  public GameStateDto createGame(@RequestBody(required = false)NewGameRequestDto request) {
    int size = request != null ? request.getSize() : 5;
    String difficulty = request != null ? request.getDifficulty() : "EASY";
    return gameService.createGame(size, difficulty);
  }

  @GetMapping("/games/{gameId}")
  public GameStateDto getGame(@PathVariable String gameId) {
    return gameService.getGame(gameId);
  }

  @PostMapping("/games/{gameId}/move")
  public GameStateDto makeMove(
          @PathVariable String gameId,
          @RequestBody MoveDto moveDto
  ) {
    return gameService.makeMove(gameId, moveDto);
  }

  @PostMapping("/games/{gameId}/clear")
  public GameStateDto clearCell(
          @PathVariable String gameId,
          @RequestParam int row,
          @RequestParam int col
  ) {
    return gameService.clearCell(gameId, row, col);
  }

  @PostMapping("/games/{gameId}/solve")
  public GameStateDto solveGame(@PathVariable String gameId) {
    return gameService.solveGame(gameId);
  }

  @PostMapping("/games/{gameId}/reset")
  public GameStateDto resetBoard(@PathVariable String gameId) {
    return gameService.resetBoard(gameId);
  }

  @PostMapping("/games/{gameId}/hint")
  public GameStateDto useHint(@PathVariable String gameId) {
    return gameService.useHint(gameId);
  }

  @PostMapping("/games/{gameId}/score")
  public ResponseEntity<Score> submitScore(
          @PathVariable String gameId,
          HttpServletRequest request
  ) throws Exception {
    String token = authCookieService.extractToken(request);

    if (token == null || !jwtService.isTokenValid(token)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    String username = jwtService.extractUsername(token);
    User user = userService.findByUsername(username);

    if (user == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    GameSession session = gameService.getSession(gameId);

    if (!session.getBoard().getState().name().equals("SOLVED")) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    if (session.getBoard().getIsSolvedAutomatically()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    if (session.isScoreSubmitted()) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    int points = scoreCalculator.calculateScore(
            session.getBoard().getSize(),
            session.getBoard().getDifficulty(),
            session.getElapsedSeconds(),
            session.getHintsUsed()
    );

    Score score = new Score(
            "jigsaw sudoku",
            user.getUsername(),
            points,
            new Date()
    );

    scoreService.addScore(score);
    session.markScoreSubmitted();

    return ResponseEntity.ok(score);
  }
}
