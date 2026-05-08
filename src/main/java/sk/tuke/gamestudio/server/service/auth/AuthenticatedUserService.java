package sk.tuke.gamestudio.server.service.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sk.tuke.gamestudio.entity.User;
import sk.tuke.gamestudio.service.user.UserException;
import sk.tuke.gamestudio.service.user.UserService;

@Service
public class AuthenticatedUserService {
  private final AuthCookieService authCookieService;
  private final JwtService jwtService;
  private final UserService userService;

  public AuthenticatedUserService(
          AuthCookieService authCookieService,
          JwtService jwtService,
          UserService userService
  ) {
    this.authCookieService = authCookieService;
    this.jwtService = jwtService;
    this.userService = userService;
  }

  public User getAuthenticatedUser(HttpServletRequest request) {
    String token = authCookieService.extractToken(request);

    if (token == null || !jwtService.isTokenValid(token)) {
      throw new ResponseStatusException(
              HttpStatus.UNAUTHORIZED,
              "Authentication required"
      );
    }

    String username = jwtService.extractUsername(token);

    try {
      User user = userService.findByUsername(username);

      if (user == null) {
        throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Authentication required"
        );
      }

      return user;
    } catch (UserException e) {
      throw new ResponseStatusException(
              HttpStatus.UNAUTHORIZED,
              "Authentication required"
      );
    }
  }
}