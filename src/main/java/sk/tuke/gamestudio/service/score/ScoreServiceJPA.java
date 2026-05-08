package sk.tuke.gamestudio.service.score;

import sk.tuke.gamestudio.entity.Score;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

@Transactional
public class ScoreServiceJPA implements ScoreService {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public void addScore(Score score) throws ScoreException {
    entityManager.persist(score);
  }

  @Override
  public List<Score> getTopScores(String game) throws ScoreException {
    return entityManager
            .createNamedQuery("Score.getTopScores", Score.class)
            .setParameter("game", game)
            .setMaxResults(10).getResultList();
  }

  @Override
  public void reset() {
    entityManager.createNamedQuery("Score.resetScores").executeUpdate();
  }
}