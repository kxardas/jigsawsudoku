package sk.tuke.gamestudio.server.service.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthCookieService {
  private static final String COOKIE_NAME = "accessToken";
  private static final int MAX_AGE_SECONDS = 60 * 60 * 24; // 24h

  public void addAuthCookie(HttpServletResponse res, String token) {
    Cookie cookie = new Cookie(COOKIE_NAME, token);

    cookie.setHttpOnly(true);
    cookie.setSecure(false); // dev only, true in production with HTTPS
    cookie.setPath("/");
    cookie.setMaxAge(MAX_AGE_SECONDS);

    res.addCookie(cookie);
  }

  public void clearAuthCookie(HttpServletResponse res) {
    Cookie cookie = new Cookie(COOKIE_NAME, "");

    cookie.setHttpOnly(true);
    cookie.setSecure(false); // dev only
    cookie.setPath("/");
    cookie.setMaxAge(0);

    res.addCookie(cookie);
  }

  public String extractToken(HttpServletRequest req) {
    if (req.getCookies() == null) {
      return null;
    }

    for (Cookie cookie : req.getCookies()) {
      if (COOKIE_NAME.equals(cookie.getName())) {
        return cookie.getValue();
      }
    }

    return null;
  }
}
