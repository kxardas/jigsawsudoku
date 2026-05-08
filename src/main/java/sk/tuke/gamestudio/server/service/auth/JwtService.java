package sk.tuke.gamestudio.server.service.auth;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sk.tuke.gamestudio.entity.User;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {
  private static final long EXPIRATION_MS = 1000L * 60 * 60 * 24; // 24h

  private final SecretKey key;

  public JwtService(@Value("${jwt.secret}") String secret) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  public String generateToken(User user) {
    Date now = new Date();
    Date expiresAt = new Date(now.getTime() + EXPIRATION_MS);

    return Jwts.builder()
            .subject(user.getUsername())
            .claim("id", user.getIdent())
            .claim("role", user.getRole())
            .issuedAt(now)
            .expiration(expiresAt)
            .signWith(key)
            .compact();
  }

  public String extractUsername(String token) {
    return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
  }

  public boolean isTokenValid(String token) {
    try {
      extractUsername(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }
}