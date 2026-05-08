package sk.tuke.gamestudio.service.rating;

import jakarta.persistence.NoResultException;
import sk.tuke.gamestudio.entity.Rating;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.Date;
import java.util.List;

@Transactional
public class RatingServiceJPA implements RatingService {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public void setRating(Rating rating) throws RatingException {
    Date createdAt = new Date();

    Rating existingRating = findRating(rating.getGame(), rating.getUsername());

    if (existingRating == null) {
      rating.setCreatedAt(createdAt);
      entityManager.persist(rating);
      return;
    }

    existingRating.setValue(rating.getValue());
    existingRating.setCreatedAt(createdAt);
  }

  @Override
  public double getAverageRating(String game) throws RatingException {
    List<Rating> ratings = entityManager
            .createNamedQuery("Rating.getAllRatings", Rating.class)
            .setParameter("game", game).getResultList();

    double r = 0;
    for (Rating rating : ratings) {
      r += rating.getValue();
    }

    return ratings.isEmpty() ? 0 : r / ratings.size();
  }

  @Override
  public int getRating(String game, String username) throws RatingException {
    Rating rating = findRating(game, username);

    return rating == null ? 0 : rating.getValue();
  }

  @Override
  public void reset() {
    entityManager.createNamedQuery("Rating.resetRatings").executeUpdate();
  }

  private Rating findRating(String game, String username) {
    try {
      return entityManager
              .createNamedQuery("Rating.getRating", Rating.class)
              .setParameter("game", game)
              .setParameter("username", username)
              .getSingleResult();
    } catch (NoResultException ex) {
      return null;
    }
  }
}
