package sk.tuke.gamestudio.service.user;

import sk.tuke.gamestudio.entity.User;

public interface UserService {
  User findByUsername(String username) throws UserException;
  boolean existsByUsername(String username) throws UserException;
  void createUser(User user) throws UserException;
}
