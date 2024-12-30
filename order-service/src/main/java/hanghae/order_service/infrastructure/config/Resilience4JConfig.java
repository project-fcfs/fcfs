package hanghae.order_service.infrastructure.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.spring6.circuitbreaker.configure.CircuitBreakerConfigurationProperties;
import io.github.resilience4j.spring6.retry.configure.RetryConfigurationProperties;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "resilience4j.circuitbreaker.configs.default")
public class Resilience4JConfig {

    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> circuitBreakerCustomizer() {

        CircuitBreakerConfigurationProperties prop = new CircuitBreakerConfigurationProperties();
        RetryConfigurationProperties retryProp = new RetryConfigurationProperties();
        prop.setCircuitBreakerAspectOrder(1);
        retryProp.setRetryAspectOrder(3);

        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .circuitBreakerConfig(customCircuitBreakerConfig())
                .build());
    }

    @Bean
    public CircuitBreakerConfig customCircuitBreakerConfig() {
        return CircuitBreakerConfig.custom()
                .slidingWindowType(SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(100)
                .failureRateThreshold(10)
                .minimumNumberOfCalls(50)
                .slowCallRateThreshold(10)
                .slowCallDurationThreshold(Duration.ofMillis(3000))
                .permittedNumberOfCallsInHalfOpenState(30)
                .maxWaitDurationInHalfOpenState(Duration.ofMillis(3000))
                .waitDurationInOpenState(Duration.ofMillis(1000))
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .build();
    }

    @Bean
    public Retry retryCustomizer() {
        RetryConfig retryConfig = customRetryConfig();
        RetryRegistry retryRegistry = RetryRegistry.of(retryConfig);
        return retryRegistry.retry("customRetry",
                retryConfig);
    }

    @Bean
    public RetryConfig customRetryConfig() {
        return RetryConfig.custom()
                .maxAttempts(2)
                .waitDuration(Duration.ofMillis(100))
                .build();
    }
}
