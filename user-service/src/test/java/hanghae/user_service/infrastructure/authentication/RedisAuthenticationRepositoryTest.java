/*
package hanghae.user_service.infrastructure.authentication;

import static org.assertj.core.api.Assertions.assertThat;

import hanghae.user_service.testSupport.IntegrationInfraTestSupport;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;


class RedisAuthenticationRepositoryTest extends IntegrationInfraTestSupport {

    @Autowired
    RedisAuthenticationRepository repository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    @DisplayName("레디스에 만료기한을 주고 저장을 할 수 있다")
    @Disabled
    void createExpirationRedis() throws Exception {
        // given
        String key = "testKey";
        String value = "testValue";
        Long expirationKey = 1000 * 5L;

        // when
        repository.save(key, value, expirationKey);
        String result = (String) redisTemplate.opsForValue().get(key);

        // then
        assertThat(result).isEqualTo(value);
    }

    @Test
    @DisplayName("레디스 만료기한이 끝나면 Null을 반환한다")
    @Disabled
    void expirationTime() throws Exception {
        // given
        String key = "testKey";
        String value = "testValue";
        repository.save(key, value, 1L);

        // when
        boolean result = repository.existsCode(key, value);

        // then
        assertThat(result).isFalse();
    }


}*/
