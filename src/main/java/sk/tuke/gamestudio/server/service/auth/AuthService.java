package sk.tuke.gamestudio.server.service.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sk.tuke.gamestudio.entity.User;
import sk.tuke.gamestudio.server.dto.auth.AuthResponseDto;
import sk.tuke.gamestudio.server.dto.auth.LoginRequestDto;
import sk.tuke.gamestudio.server.dto.auth.RegisterRequestDto;
import sk.tuke.gamestudio.server.dto.auth.UserDto;
import sk.tuke.gamestudio.service.user.UserException;
import sk.tuke.gamestudio.service.user.UserService;

import java.util.Date;

@Service
public class AuthService {
  private final UserService userService;
  private final PasswordEncoder passwordEncoder;

  public AuthService(UserService userService, PasswordEncoder passwordEncoder) {
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
  }

  public AuthResponseDto register(RegisterRequestDto req) {
    if (req == null) {
      throw new AuthException("Register request cannot be empty");
    }

    String username = req.getUsername() == null ? "" : req.getUsername().trim();
    String password = req.getPassword();
    String confirmPassword = req.getConfirmPassword();

    if (username.length() < 4) {
      throw new AuthException("Username must be at least 4 characters");
    }

    if (password == null || password.length() < 4) {
      throw new AuthException("Password must be at least 4 characters");
    }

    if (!password.equals(confirmPassword)) {
      throw new AuthException("Passwords do not match");
    }

    try {
      if (userService.existsByUsername(username)) {
        throw new AuthException("Username is already taken");
      }

      String passwordHash = passwordEncoder.encode(password);

      User user = new User(
              username,
              passwordHash,
              "USER",
              new Date()
      );

      userService.createUser(user);

      User createdUser = userService.findByUsername(username);

      if (createdUser == null) {
        throw new AuthException("Could not create user");
      }

      return new AuthResponseDto(toUserDto(createdUser));
    } catch (UserException e) {
      throw new AuthException("Could not register user", e);
    }
  }

  public AuthResponseDto login(LoginRequestDto req) {
    if (req == null) {
      throw new AuthException("Login request cannot be empty");
    }

    String username = req.getUsername() == null ? "" : req.getUsername().trim();
    String password = req.getPassword();

    if (username.isEmpty()) {
      throw new AuthException("Username cannot be empty");
    }

    if (password == null || password.isEmpty()) {
      throw new AuthException("Password cannot be empty");
    }
    try {
      User user = userService.findByUsername(username);

      if (user == null) {
        throw new AuthException("Invalid username or password");
      }

      boolean passwordMatches = passwordEncoder.matches(
              password,
              user.getPasswordHash()
      );

      if (!passwordMatches) {
        throw new AuthException("Invalid username or password");
      }

      return new AuthResponseDto(toUserDto(user));
    } catch (UserException e) {
      throw new AuthException("Could not log in user", e);
    }
  }

  public UserDto toUserDto(User user) {
    return new UserDto(
            user.getIdent(),
            user.getUsername(),
            user.getRole()
    );
  }
}
