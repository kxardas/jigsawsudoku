package sk.tuke.gamestudio.server.service.auth;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisRateLimitService {
  private final StringRedisTemplate redisTemplate;

  public RedisRateLimitService(StringRedisTemplate redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  public boolean tryConsume(String key, int limit, Duration window) {
    Long count = redisTemplate.opsForValue().increment(key);

    if (count == null) {
      return false;
    }

    if (count == 1) {
      redisTemplate.expire(key, window);
    }

    return count <= limit;
  }
}
