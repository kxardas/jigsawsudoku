package sk.tuke.gamestudio.server;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.entity.User;
import sk.tuke.gamestudio.service.comment.CommentServiceJPA;
import sk.tuke.gamestudio.service.rating.RatingServiceJPA;
import sk.tuke.gamestudio.service.score.ScoreServiceJPA;
import sk.tuke.gamestudio.service.user.UserServiceJPA;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:jpa-service-test;MODE=PostgreSQL;NON_KEYWORDS=VALUE;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.show-sql=false",
        "spring.jpa.properties.hibernate.show_sql=false",
        "logging.level.org.hibernate.SQL=OFF",
        "debug=false"
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({
        ScoreServiceJPA.class,
        CommentServiceJPA.class,
        RatingServiceJPA.class,
        UserServiceJPA.class
})
class JpaServiceTest {
  private static final String GAME = "jigsaw sudoku";

  @Autowired
  private ScoreServiceJPA scoreService;

  @Autowired
  private CommentServiceJPA commentService;

  @Autowired
  private RatingServiceJPA ratingService;

  @Autowired
  private UserServiceJPA userService;

  @Autowired
  private EntityManager entityManager;

  @Test
  void scoreServiceStoresOnlyTopTenScoresForGameOrderedByPoints() {
    for (int i = 1; i <= 11; i++) {
      scoreService.addScore(new Score(GAME, "player-" + i, i * 100, new Date(i * 1000L)));
    }
    scoreService.addScore(new Score("mines", "other-game", 10_000, new Date()));

    List<Score> scores = scoreService.getTopScores(GAME);

    assertEquals(10, scores.size());
    assertEquals("player-11", scores.get(0).getPlayer());
    assertEquals(1100.0, scores.get(0).getPoints(), 0.001);
    assertEquals("player-2", scores.get(9).getPlayer());
  }

  @Test
  void scoreServiceResetDeletesScores() {
    scoreService.addScore(new Score(GAME, "player", 100, new Date()));

    scoreService.reset();

    assertTrue(scoreService.getTopScores(GAME).isEmpty());
  }

  @Test
  void commentServiceReturnsCommentsForGameNewestFirst() {
    commentService.addComment(new Comment(GAME, "older", "First", new Date(1_000)));
    commentService.addComment(new Comment(GAME, "newer", "Second", new Date(2_000)));
    commentService.addComment(new Comment("mines", "other", "Hidden", new Date(3_000)));

    List<Comment> comments = commentService.getComments(GAME);

    assertEquals(2, comments.size());
    assertEquals("newer", comments.get(0).getUserName());
    assertEquals("older", comments.get(1).getUserName());
  }

  @Test
  void ratingServiceCreatesAndUpdatesOneRatingPerUserAndGame() {
    ratingService.setRating(new Rating(GAME, "alice", 4, null));
    ratingService.setRating(new Rating(GAME, "alice", 8, null));
    ratingService.setRating(new Rating(GAME, "bob", 6, null));

    assertEquals(8, ratingService.getRating(GAME, "alice"));
    assertEquals(0, ratingService.getRating(GAME, "missing"));
    assertEquals(7.0, ratingService.getAverageRating(GAME), 0.001);

    Long aliceRatingCount = entityManager
            .createQuery(
                    "SELECT COUNT(r) FROM Rating r WHERE r.game = :game AND r.username = :username",
                    Long.class
            )
            .setParameter("game", GAME)
            .setParameter("username", "alice")
            .getSingleResult();

    assertEquals(1L, aliceRatingCount);
  }

  @Test
  void userServiceCreatesFindsAndChecksUsersByUsername() {
    User user = new User("alice", "hash", "USER", new Date());

    userService.createUser(user);

    assertTrue(userService.existsByUsername("alice"));
    assertFalse(userService.existsByUsername("bob"));

    User found = userService.findByUsername("alice");

    assertNotNull(found);
    assertEquals("alice", found.getUsername());
    assertEquals("hash", found.getPasswordHash());
    assertEquals("USER", found.getRole());
  }
}
