package sk.tuke.gamestudio.service.user;

import sk.tuke.gamestudio.entity.User;

import java.sql.*;

public class UserServiceJDBC implements UserService {
  public static final String URL = "jdbc:postgresql://localhost/gamestudio";
  public static final String USER = "pavlik";
  public static final String PASSWORD = "1234";
  public static final String SELECT_BY_USERNAME =
          "SELECT ident, username, password_hash, role, created_at FROM users WHERE username = ?";
  public static final String EXISTS_BY_USERNAME =
          "SELECT COUNT(*) FROM users WHERE username = ?";
  public static final String DELETE =
          "DELETE FROM users WHERE username = ?";
  public static final String INSERT =
          "INSERT INTO users (username, password_hash, role, created_at) VALUES (?, ?, ?, ?)";
  @Override
  public User findByUsername(String username) throws UserException {
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement statement = connection.prepareStatement(SELECT_BY_USERNAME);
    ) {
      statement.setString(1, username);

      try (ResultSet rs = statement.executeQuery()) {
        if (rs.next()) {
          return new User(
                  rs.getInt("ident"),
                  rs.getString("username"),
                  rs.getString("password_hash"),
                  rs.getString("role"),
                  rs.getTimestamp("created_at")
          );
        }
        return null;
      }
    } catch (SQLException e) {
      throw new UserException("Problem selecting user", e);
    }
  }

  @Override
  public boolean existsByUsername(String username) throws UserException {
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement statement = connection.prepareStatement(EXISTS_BY_USERNAME);
    ) {
      statement.setString(1, username);

      try (ResultSet rs = statement.executeQuery()) {
        if (rs.next()) {
          return rs.getInt(1) > 0;
        }
        return false;
      }
    } catch (SQLException e) {
      throw new UserException("Problem checking user existence", e);
    }
  }

  @Override
  public void createUser(User user) throws UserException {
    try (
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement statement = connection.prepareStatement(INSERT)
    ) {
      statement.setString(1, user.getUsername());
      statement.setString(2, user.getPasswordHash());
      statement.setString(3, user.getRole());
      statement.setTimestamp(4, new Timestamp(user.getCreatedAt().getTime()));

      statement.executeUpdate();
    } catch (SQLException e) {
      throw new UserException("Problem creating user", e);
    }
  }
}
