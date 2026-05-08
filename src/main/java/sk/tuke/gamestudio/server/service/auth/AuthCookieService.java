package sk.tuke.gamestudio.server.service.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class AuthCookieService {
  private static final String COOKIE_NAME = "accessToken";
  private static final int MAX_AGE_SECONDS = 60 * 60 * 24; // 24h

  private final boolean secure;
  private final String sameSite;
  private final String domain;

  public AuthCookieService(
          @Value("${app.cookie.secure:false}") boolean secure,
          @Value("${app.cookie.same-site:Lax}") String sameSite,
          @Value("${app.cookie.domain:}") String domain
  ) {
    this.secure = secure;
    this.sameSite = sameSite;
    this.domain = domain;
  }

  public void addAuthCookie(HttpServletResponse res, String token) {
    ResponseCookie.ResponseCookieBuilder cookieBuilder = ResponseCookie
            .from(COOKIE_NAME, token)
            .httpOnly(true)
            .secure(secure)
            .path("/")
            .maxAge(Duration.ofSeconds(MAX_AGE_SECONDS))
            .sameSite(sameSite);

    if (domain != null && !domain.isBlank()) {
      cookieBuilder.domain(domain);
    }

    res.addHeader(HttpHeaders.SET_COOKIE, cookieBuilder.build().toString());
  }

  public void clearAuthCookie(HttpServletResponse res) {
    ResponseCookie.ResponseCookieBuilder cookieBuilder = ResponseCookie
            .from(COOKIE_NAME, "")
            .httpOnly(true)
            .secure(secure)
            .path("/")
            .maxAge(Duration.ZERO)
            .sameSite(sameSite);

    if (domain != null && !domain.isBlank()) {
      cookieBuilder.domain(domain);
    }

    res.addHeader(HttpHeaders.SET_COOKIE, cookieBuilder.build().toString());
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