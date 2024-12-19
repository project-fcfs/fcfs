package hanghae.user_service.infrastructure.authentication;

import hanghae.user_service.service.port.AuthenticationRepository;
import java.time.Duration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RedisAuthenticationRepository implements AuthenticationRepository {
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisAuthenticationRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void save(String key, String value, Long expiredMs) {
        redisTemplate.opsForValue().set(key, value, Duration.ofMillis(expiredMs));
    }

    @Override
    public boolean existsCode(String key, String value) {
        String authCode = (String) redisTemplate.opsForValue().get(key);
        return authCode != null && authCode.equals(value);
    }
}
