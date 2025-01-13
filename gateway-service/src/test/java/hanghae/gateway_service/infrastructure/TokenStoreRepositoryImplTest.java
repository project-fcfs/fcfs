package hanghae.gateway_service.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

/*
@SpringBootTest
class TokenStoreRepositoryImplTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private TokenStoreRepositoryImpl tokenStoreRepository;

    @Test
    @DisplayName("만료기한이 지나면 자연스럽게 사라지고 없다고 판단한다")
    @Disabled
    void expirationTest() throws Exception {
        // given
        String key = "testKey";
        String value = "testToken";
        redisTemplate.opsForSet().add(key, value);
        redisTemplate.expire(key, Duration.ofMillis(1L));

        // when
        boolean result = tokenStoreRepository.existLoginToken(key, value);

        // then
        assertThat(result).isFalse();
    }

}*/
