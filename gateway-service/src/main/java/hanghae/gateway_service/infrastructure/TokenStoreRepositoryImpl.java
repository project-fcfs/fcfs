package hanghae.gateway_service.infrastructure;


import hanghae.gateway_service.service.port.TokenStoreRepository;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class TokenStoreRepositoryImpl implements TokenStoreRepository {
    private final ReactiveRedisOperations<String, Object> redisTemplate;

    public TokenStoreRepositoryImpl(ReactiveRedisOperations<String, Object> reactiveRedisTemplate) {
        this.redisTemplate = reactiveRedisTemplate;
    }

    @Override
    public Mono<Boolean> existLoginToken(String key, String value) {
        return  redisTemplate.opsForSet().isMember(key, value);
    }
}
