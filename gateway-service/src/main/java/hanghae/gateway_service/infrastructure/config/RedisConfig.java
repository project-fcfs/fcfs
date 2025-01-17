package hanghae.gateway_service.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    private String host;
    private int port;

    public RedisConfig(@Value("${spring.data.redis.host}") String host,
                       @Value("${spring.data.redis.port}") int port) {
        this.host = host;
        this.port = port;
    }

    @Bean
    @Primary
    public ReactiveRedisConnectionFactory connectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public ReactiveRedisOperations<String, Object> reactiveRedisTemplate() {
        ReactiveRedisConnectionFactory rrcf = connectionFactory();

        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);

        RedisSerializationContext.RedisSerializationContextBuilder<String, Object> builder = RedisSerializationContext
                .newSerializationContext(new StringRedisSerializer());

        RedisSerializationContext<String, Object> context = builder.value(serializer).hashValue(serializer)
                .hashKey(serializer).build();

        return new ReactiveRedisTemplate<>(rrcf, context);
    }
}