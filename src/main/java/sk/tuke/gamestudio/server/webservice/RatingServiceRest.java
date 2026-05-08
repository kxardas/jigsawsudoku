package sk.tuke.gamestudio.server.webservice;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.User;
import sk.tuke.gamestudio.server.service.auth.AuthenticatedUserService;
import sk.tuke.gamestudio.service.rating.RatingService;

import java.util.Date;

@RestController
@RequestMapping("/api/rating")
public class RatingServiceRest {

  private final RatingService ratingService;
  private final AuthenticatedUserService authenticatedUserService;

  public RatingServiceRest(
          RatingService ratingService,
          AuthenticatedUserService authenticatedUserService
  ) {
    this.ratingService = ratingService;
    this.authenticatedUserService = authenticatedUserService;
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
  public void setRating(
          @RequestBody Rating rating,
          HttpServletRequest request
  ) {
    User user = authenticatedUserService.getAuthenticatedUser(request);

    if (rating.getGame() == null || rating.getGame().isBlank()) {
      throw new ResponseStatusException(
              HttpStatus.BAD_REQUEST,
              "Game name is required"
      );
    }

    if (rating.getValue() < 1 || rating.getValue() > 5) {
      throw new ResponseStatusException(
              HttpStatus.BAD_REQUEST,
              "Rating must be between 1 and 5"
      );
    }

    Rating safeRating = new Rating(
            rating.getGame(),
            user.getUsername(),
            rating.getValue(),
            new Date()
    );

    ratingService.setRating(safeRating);
  }
}