package sk.tuke.gamestudio.server.webservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.server.dto.GameStateDto;
import sk.tuke.gamestudio.server.dto.MoveDto;
import sk.tuke.gamestudio.server.service.JigsawSudokuGameService;
import sk.tuke.gamestudio.server.service.ScoreCalculator;
import sk.tuke.gamestudio.server.service.auth.AuthCookieService;
import sk.tuke.gamestudio.server.service.auth.JwtService;
import sk.tuke.gamestudio.service.comment.CommentService;
import sk.tuke.gamestudio.service.comment.CommentException;
import sk.tuke.gamestudio.service.rating.RatingService;
import sk.tuke.gamestudio.service.rating.RatingException;
import sk.tuke.gamestudio.service.score.ScoreService;
import sk.tuke.gamestudio.service.score.ScoreException;
import sk.tuke.gamestudio.service.user.UserException;
import sk.tuke.gamestudio.service.user.UserService;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RestControllerTest {
  private static final String GAME = "jigsaw sudoku";

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void scoreRestReturnsTopScoresAndAcceptsPostedScore() throws Exception {
    CapturingScoreService scoreService = new CapturingScoreService();
    MockMvc mockMvc = MockMvcBuilders
            .standaloneSetup(new ScoreServiceRest(scoreService))
            .build();

    scoreService.topScores = List.of(new Score(GAME, "alice", 500, new Date(1_000)));

    mockMvc.perform(get("/api/score/{game}", GAME))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].game").value(GAME))
            .andExpect(jsonPath("$[0].player").value("alice"))
            .andExpect(jsonPath("$[0].points").value(500.0));

    mockMvc.perform(post("/api/score")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            {
                              "game": "jigsaw sudoku",
                              "player": "bob",
                              "points": 250,
                              "playedOn": 1000
                            }
                            """))
            .andExpect(status().isOk());

    assertEquals("bob", scoreService.addedScore.getPlayer());
    assertEquals(250.0, scoreService.addedScore.getPoints(), 0.001);
  }

  @Test
  void commentRestReturnsCommentsAndAcceptsPostedComment() throws Exception {
    CapturingCommentService commentService = new CapturingCommentService();
    MockMvc mockMvc = MockMvcBuilders
            .standaloneSetup(new CommentServiceRest(commentService))
            .build();

    commentService.comments = List.of(new Comment(GAME, "alice", "Nice puzzle", new Date(1_000)));

    mockMvc.perform(get("/api/comment/{game}", GAME))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].game").value(GAME))
            .andExpect(jsonPath("$[0].userName").value("alice"))
            .andExpect(jsonPath("$[0].text").value("Nice puzzle"));

    mockMvc.perform(post("/api/comment")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            {
                              "game": "jigsaw sudoku",
                              "userName": "bob",
                              "text": "Solved it",
                              "createdAt": 1000
                            }
                            """))
            .andExpect(status().isOk());

    assertEquals("bob", commentService.addedComment.getUserName());
    assertEquals("Solved it", commentService.addedComment.getText());
  }

  @Test
  void ratingRestReturnsAverageUserRatingAndAcceptsRating() throws Exception {
    CapturingRatingService ratingService = new CapturingRatingService();
    MockMvc mockMvc = MockMvcBuilders
            .standaloneSetup(new RatingServiceRest(ratingService))
            .build();

    ratingService.averageRating = 7.5;
    ratingService.userRating = 8;

    mockMvc.perform(get("/api/rating/{game}", GAME))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(7.5));

    mockMvc.perform(get("/api/rating/{game}/{username}", GAME, "alice"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(8));

    mockMvc.perform(post("/api/rating")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            {
                              "game": "jigsaw sudoku",
                              "username": "bob",
                              "value": 9,
                              "createdAt": 1000
                            }
                            """))
            .andExpect(status().isOk());

    assertEquals("bob", ratingService.addedRating.getUsername());
    assertEquals(9, ratingService.addedRating.getValue());
  }

  @Test
  void engineRestCreatesGameAndDelegatesMovePayload() throws Exception {
    FakeGameService gameService = new FakeGameService();
    MockMvc mockMvc = MockMvcBuilders
            .standaloneSetup(jigsawSudokuRest(gameService))
            .setControllerAdvice(new RestExceptionHandler())
            .build();

    GameStateDto createdGame = new GameStateDto(
            "game-1",
            5,
            new int[5][5],
            new boolean[5][5],
            new int[5][5],
            "EASY",
            "PLAYING",
            false,
            true,
            "Game created",
            3,
            0,
            false,
            300
    );

    gameService.response = createdGame;

    mockMvc.perform(post("/api/engine/games")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            {
                              "size": 5,
                              "difficulty": "EASY"
                            }
                            """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.gameId").value("game-1"))
            .andExpect(jsonPath("$.state").value("PLAYING"));

    mockMvc.perform(post("/api/engine/games/{gameId}/move", "game-1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(new MoveDto(1, 2, 3))))
            .andExpect(status().isOk());

    assertEquals(5, gameService.createdSize);
    assertEquals("EASY", gameService.createdDifficulty);
    assertEquals("game-1", gameService.movedGameId);
    assertEquals(1, gameService.move.getRow());
    assertEquals(2, gameService.move.getCol());
    assertEquals(3, gameService.move.getValue());
  }

  @Test
  void engineRestMapsBadGameIdToBadRequest() throws Exception {
    FakeGameService gameService = new FakeGameService();
    MockMvc mockMvc = MockMvcBuilders
            .standaloneSetup(jigsawSudokuRest(gameService))
            .setControllerAdvice(new RestExceptionHandler())
            .build();

    gameService.getGameException = new IllegalArgumentException("No such game: missing");

    mockMvc.perform(get("/api/engine/games/{gameId}", "missing"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("No such game: missing"));
  }

  private JigsawSudokuRest jigsawSudokuRest(JigsawSudokuGameService gameService) {
    return new JigsawSudokuRest(
            gameService,
            new AuthCookieService(),
            new JwtService(),
            new EmptyUserService(),
            new ScoreCalculator(),
            new CapturingScoreService()
    );
  }

  private static class CapturingScoreService implements ScoreService {
    private List<Score> topScores = List.of();
    private Score addedScore;

    @Override
    public void addScore(Score score) throws ScoreException {
      addedScore = score;
    }

    @Override
    public List<Score> getTopScores(String game) throws ScoreException {
      return topScores;
    }

    @Override
    public void reset() throws ScoreException {
      topScores = List.of();
    }
  }

  private static class CapturingCommentService implements CommentService {
    private List<Comment> comments = List.of();
    private Comment addedComment;

    @Override
    public void addComment(Comment comment) throws CommentException {
      addedComment = comment;
    }

    @Override
    public List<Comment> getComments(String game) throws CommentException {
      return comments;
    }

    @Override
    public void reset() throws CommentException {
      comments = List.of();
    }
  }

  private static class CapturingRatingService implements RatingService {
    private double averageRating;
    private int userRating;
    private Rating addedRating;

    @Override
    public void setRating(Rating rating) throws RatingException {
      addedRating = rating;
    }

    @Override
    public double getAverageRating(String game) throws RatingException {
      return averageRating;
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
      return userRating;
    }

    @Override
    public void reset() throws RatingException {
      averageRating = 0;
      userRating = 0;
    }
  }

  private static class EmptyUserService implements UserService {
    @Override
    public sk.tuke.gamestudio.entity.User findByUsername(String username) throws UserException {
      return null;
    }

    @Override
    public boolean existsByUsername(String username) throws UserException {
      return false;
    }

    @Override
    public void createUser(sk.tuke.gamestudio.entity.User user) throws UserException {
    }
  }

  private static class FakeGameService extends JigsawSudokuGameService {
    private GameStateDto response;
    private RuntimeException getGameException;
    private int createdSize;
    private String createdDifficulty;
    private String movedGameId;
    private MoveDto move;

    private FakeGameService() {
      super(new ScoreCalculator());
    }

    @Override
    public GameStateDto createGame(int size, String difficultyValue) {
      createdSize = size;
      createdDifficulty = difficultyValue;

      return response;
    }

    @Override
    public GameStateDto makeMove(String gameId, MoveDto moveDto) {
      movedGameId = gameId;
      move = moveDto;

      return response;
    }

    @Override
    public GameStateDto getGame(String gameId) {
      if (getGameException != null) {
        throw getGameException;
      }

      return response;
    }
  }
}
