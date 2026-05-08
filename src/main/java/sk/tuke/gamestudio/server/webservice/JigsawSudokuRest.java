package sk.tuke.gamestudio.server.webservice;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.entity.User;
import sk.tuke.gamestudio.server.dto.GameStateDto;
import sk.tuke.gamestudio.server.dto.MoveDto;
import sk.tuke.gamestudio.server.dto.NewGameRequestDto;
import sk.tuke.gamestudio.server.service.GameSession;
import sk.tuke.gamestudio.server.service.JigsawSudokuGameService;
import sk.tuke.gamestudio.server.service.ScoreCalculator;
import sk.tuke.gamestudio.server.service.auth.AuthenticatedUserService;
import sk.tuke.gamestudio.service.score.ScoreService;

import java.util.Date;

@RestController
@RequestMapping("/api/engine")
public class JigsawSudokuRest {

  private final JigsawSudokuGameService gameService;
  private final AuthenticatedUserService authenticatedUserService;
  private final ScoreCalculator scoreCalculator;
  private final ScoreService scoreService;

  public JigsawSudokuRest(
          JigsawSudokuGameService gameService,
          AuthenticatedUserService authenticatedUserService,
          ScoreCalculator scoreCalculator,
          ScoreService scoreService
  ) {
    this.gameService = gameService;
    this.authenticatedUserService = authenticatedUserService;
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
    User user = authenticatedUserService.getAuthenticatedUser(request);

    GameSession session = gameService.getSession(gameId);

    if (!session.getBoard().getState().name().equals("SOLVED")) {
      throw new ResponseStatusException(
              HttpStatus.BAD_REQUEST,
              "Game is not solved yet"
      );
    }

    if (session.getBoard().getIsSolvedAutomatically()) {
      throw new ResponseStatusException(
              HttpStatus.BAD_REQUEST,
              "Automatically solved games cannot be submitted"
      );
    }

    if (session.isScoreSubmitted()) {
      throw new ResponseStatusException(
              HttpStatus.CONFLICT,
              "Score has already been submitted"
      );
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
