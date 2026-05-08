package sk.tuke.gamestudio.service.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import sk.tuke.gamestudio.entity.User;

@Transactional
public class UserServiceJPA implements UserService {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public User findByUsername(String username) throws UserException {
    try {
      return entityManager
              .createNamedQuery("GameUser.findByUsername", User.class)
              .setParameter("username", username)
              .getResultStream()
              .findFirst()
              .orElse(null);
    } catch (Exception e) {
      throw new UserException("Problem selecting user", e);
    }
  }

  @Override
  public boolean existsByUsername(String username) throws UserException {
    try {
      Long count = entityManager
              .createNamedQuery("GameUser.existsByUsername", Long.class)
              .setParameter("username", username)
              .getSingleResult();

      return count > 0;
    } catch (Exception e) {
      throw new UserException("Problem checking user existence", e);
    }
  }

  @Override
  @Transactional
  public void createUser(User user) throws UserException {
    try {
      entityManager.persist(user);
    } catch (Exception e) {
      throw new UserException("Problem creating user", e);
    }
  }
}