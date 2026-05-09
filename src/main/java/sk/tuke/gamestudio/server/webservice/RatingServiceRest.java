package sk.tuke.gamestudio.server.webservice;

import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.service.rating.RatingService;

@RestController
@RequestMapping("/api/rating")
public class RatingServiceRest {

  private final RatingService ratingService;

  public RatingServiceRest(RatingService ratingService) {
    this.ratingService = ratingService;
  }

  @GetMapping("/{game}")
  public double getAverageRating(@PathVariable String game) {
    return ratingService.getAverageRating(game);
  }

  @GetMapping("/{game}/{username}")
  public int getRating(@PathVariable String game, @PathVariable String username) {
    return ratingService.getRating(game, username);
  }

  @PostMapping
  public void setRating(@RequestBody Rating rating) {
    ratingService.setRating(rating);
  }
}
