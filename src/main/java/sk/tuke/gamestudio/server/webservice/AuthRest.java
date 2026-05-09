package sk.tuke.gamestudio.server.webservice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sk.tuke.gamestudio.entity.User;
import sk.tuke.gamestudio.server.dto.auth.AuthResponseDto;
import sk.tuke.gamestudio.server.dto.auth.LoginRequestDto;
import sk.tuke.gamestudio.server.dto.auth.RegisterRequestDto;
import sk.tuke.gamestudio.server.dto.auth.UserDto;
import sk.tuke.gamestudio.server.service.auth.AuthCookieService;
import sk.tuke.gamestudio.server.service.auth.AuthService;
import sk.tuke.gamestudio.server.service.auth.JwtService;
import sk.tuke.gamestudio.server.service.auth.RedisRateLimitService;
import sk.tuke.gamestudio.service.user.UserException;
import sk.tuke.gamestudio.service.user.UserService;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
public class AuthRest {
  private final AuthService authService;
  private final JwtService jwtService;
  private final AuthCookieService authCookieService;
  private final UserService userService;
  private final RedisRateLimitService rateLimitService;

  public AuthRest(
          AuthService authService,
          JwtService jwtService,
          AuthCookieService authCookieService,
          UserService userService,
          RedisRateLimitService rateLimitService
  ) {
    this.authService = authService;
    this.jwtService = jwtService;
    this.authCookieService = authCookieService;
    this.userService = userService;
    this.rateLimitService = rateLimitService;
  }

  @PostMapping("/register")
  public AuthResponseDto register(
          @RequestBody RegisterRequestDto req,
          HttpServletResponse res,
          HttpServletRequest request
  ) throws UserException {
    String ip = getClientIp(request);

    boolean allowed = rateLimitService.tryConsume(
            "rate:register:" + ip,
            5,
            Duration.ofMinutes(10)
    );

    if (!allowed) {
      throw new ResponseStatusException(
              HttpStatus.TOO_MANY_REQUESTS,
              "Too many registration attempts. Try again later."
      );
    }

    AuthResponseDto authResponse = authService.register(req);

    User user = userService.findByUsername(authResponse.getUser().getUsername());
    String token = jwtService.generateToken(user);

    authCookieService.addAuthCookie(res, token);

    return authResponse;
  }

  @PostMapping("/login")
  public AuthResponseDto login(
          @RequestBody LoginRequestDto req,
          HttpServletResponse res,
          HttpServletRequest request
  ) throws UserException {
    String ip = getClientIp(request);

    String username = req.getUsername() == null ? "unknown" : req.getUsername().trim().toLowerCase();
    boolean ipAllowed = rateLimitService.tryConsume(
            "rate:login:ip:" + ip,
            20,
            Duration.ofMinutes(10)
    );

    boolean usernameAllowed = rateLimitService.tryConsume(
            "rate:login:username:" + username,
            10,
            Duration.ofMinutes(10)
    );

    if (!ipAllowed || !usernameAllowed) {
      throw new ResponseStatusException(
              HttpStatus.TOO_MANY_REQUESTS,
              "Too many login attempts. Try again later."
      );
    }

    AuthResponseDto authResponse = authService.login(req);

    User user = userService.findByUsername(authResponse.getUser().getUsername());
    String token = jwtService.generateToken(user);

    authCookieService.addAuthCookie(res, token);

    return authResponse;
  }

  @GetMapping("/me")
  public ResponseEntity<UserDto> me(HttpServletRequest req) throws UserException {
    String token = authCookieService.extractToken(req);

    if (token == null || !jwtService.isTokenValid(token)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    String username = jwtService.extractUsername(token);
    User user = userService.findByUsername(username);

    if (user == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    return ResponseEntity.ok(authService.toUserDto(user));
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(HttpServletResponse res) {
    authCookieService.clearAuthCookie(res);

    return ResponseEntity.noContent().build();
  }

  private String getClientIp(HttpServletRequest req) {
    String forwardedFor = req.getHeader("X-Forwarded-For");

    if (forwardedFor != null && !forwardedFor.isBlank()) {
      return forwardedFor.split(",")[0].trim();
    }

    return req.getRemoteAddr();
  }
}