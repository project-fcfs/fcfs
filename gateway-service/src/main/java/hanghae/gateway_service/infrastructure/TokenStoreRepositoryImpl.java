package hanghae.gateway_service.infrastructure;


import hanghae.gateway_service.service.port.TokenStoreRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TokenStoreRepositoryImpl implements TokenStoreRepository {
    private final RedisTemplate<String, Object> redisTemplate;

    public TokenStoreRepositoryImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean existLoginToken(String key, String value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }
}
