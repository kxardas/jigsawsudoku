package sk.tuke.gamestudio;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.game.jigsawsudoku.consoleui.ConsoleUI;
import sk.tuke.gamestudio.service.comment.CommentService;
import sk.tuke.gamestudio.service.comment.CommentServiceRestClient;
import sk.tuke.gamestudio.service.rating.RatingService;
import sk.tuke.gamestudio.service.rating.RatingServiceRestClient;
import sk.tuke.gamestudio.service.score.ScoreService;
import sk.tuke.gamestudio.service.score.ScoreServiceRestClient;

@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})
@ComponentScan(
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.REGEX,
                pattern = "sk.tuke.gamestudio.server.*"
        )
)
public class SpringClient {
  public static void main(String[] args) {
    new SpringApplicationBuilder(SpringClient.class).web(WebApplicationType.NONE).run(args);
  }

  @Bean
  @ConditionalOnProperty(name = "gamestudio.console.enabled", havingValue = "true", matchIfMissing = true)
  public CommandLineRunner runner(ConsoleUI ui) {
    return args -> ui.showMenu();
  }

  @Bean
  public ConsoleUI consoleUI() {
    return new ConsoleUI();
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean
  public ScoreService scoreService() {
//    return new ScoreServiceJPA();
    return new ScoreServiceRestClient();
  }

  @Bean
  public RatingService ratingService() {
//    return new RatingServiceJPA();
    return new RatingServiceRestClient();
  }

  @Bean
  public CommentService commentService() {
//    return new CommentServiceJPA();
    return new CommentServiceRestClient();
  }
}
