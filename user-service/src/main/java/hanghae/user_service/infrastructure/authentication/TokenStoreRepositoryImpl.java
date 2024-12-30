package hanghae.user_service.infrastructure.authentication;

import hanghae.user_service.service.port.TokenStoreRepository;
import java.time.Duration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class TokenStoreRepositoryImpl implements TokenStoreRepository {
    private final RedisTemplate<String, Object> redisTemplate;

    public TokenStoreRepositoryImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Transactional
    @Override
    public void save(String key, String value, Long expirationTime) {
        redisTemplate.opsForSet().add(key, value);
        redisTemplate.expire(key, Duration.ofMillis(expirationTime));
    }

    @Override
    public void deleteToken(String key, String value) {
        redisTemplate.opsForSet().remove(key, value);
    }
}
