package hanghae.order_service.resilience;

import hanghae.order_service.support.IntegrationResilienceTestSupport;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class CustomResilienceTest extends IntegrationResilienceTestSupport {

    @Autowired
    private CustomCircuitBreaker customCircuitBreaker;

    @Autowired
    private CustomRetry customRetry;

    @Test
    @DisplayName("CircuitBreaker 동작 테스트")
    void canCircuitBreaker() throws Exception {
        // given
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("my-circuit");
        circuitBreaker.transitionToOpenState();

        // when
        for (int i = 0; i < 20; i++) {
            customCircuitBreaker.myCircuit(i);
        }

        // then
    }

    @Test
    @DisplayName("Retry 동작 테스트")
    void canRetryTest() throws Exception {
        // given
        Retry retry = retryRegistry.retry("my-retry");

        // when
        for (int i = 0; i <10; i++) {
            customRetry.myRetryException(i);
        }

        // then
    }

}
