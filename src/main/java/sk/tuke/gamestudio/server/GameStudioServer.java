package sk.tuke.gamestudio.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.server.service.JigsawSudokuGameService;
import sk.tuke.gamestudio.server.service.ScoreCalculator;
import sk.tuke.gamestudio.service.comment.CommentService;
import sk.tuke.gamestudio.service.comment.CommentServiceJPA;
import sk.tuke.gamestudio.service.rating.RatingService;
import sk.tuke.gamestudio.service.rating.RatingServiceJPA;
import sk.tuke.gamestudio.service.score.ScoreService;
import sk.tuke.gamestudio.service.score.ScoreServiceJPA;
import sk.tuke.gamestudio.service.user.UserService;
import sk.tuke.gamestudio.service.user.UserServiceJPA;

@SpringBootApplication
@Configuration
@EntityScan("sk.tuke.gamestudio.entity")
public class GameStudioServer {
  public static void main(String[] args) {
    SpringApplication.run(GameStudioServer.class, args);
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean
  public ScoreService scoreService() {
    return new ScoreServiceJPA();
  }

  @Bean
  public RatingService ratingService() {
    return new RatingServiceJPA();
  }

  @Bean
  public CommentService commentService() {
    return new CommentServiceJPA();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public JigsawSudokuGameService jigsawSudokuGameService(ScoreCalculator scoreCalculator) {
    return new JigsawSudokuGameService(scoreCalculator);
  }

  @Bean
  public UserService userService() {
    return new UserServiceJPA();
  }

  @Bean
  public ScoreCalculator scoreCalculator() {
    return new ScoreCalculator();
  }
}