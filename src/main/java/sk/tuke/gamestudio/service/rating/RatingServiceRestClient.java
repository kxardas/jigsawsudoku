package sk.tuke.gamestudio.service.rating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;

public class RatingServiceRestClient implements RatingService {
  private final String url = "http://localhost:8080/api/rating";

  @Autowired
  private RestTemplate restTemplate;

  @Override
  public void setRating(Rating rating) {
    restTemplate.postForEntity(url, rating, Score.class);
  }

  @Override
  public double getAverageRating(String game) throws RatingException {
    return restTemplate.getForEntity(url + "/" + game, Integer.class).getBody();
  }

  @Override
  public int getRating(String game, String username) {
    return restTemplate.getForEntity(url + "/" + game + "/" + username, Integer.class).getBody();
  }

  @Override
  public void reset() {
    throw new UnsupportedOperationException("Not supported via web service");
  }
}