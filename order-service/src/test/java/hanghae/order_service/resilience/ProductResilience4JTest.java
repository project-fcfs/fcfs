package hanghae.order_service.resilience;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import hanghae.order_service.service.CartService;
import hanghae.order_service.service.common.exception.CustomApiException;
import hanghae.order_service.service.common.util.ErrorMessage;
import hanghae.order_service.support.IntegrationResilienceTestSupport;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreaker.State;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ProductResilience4JTest extends IntegrationResilienceTestSupport {

    @Autowired
    protected CartService cartService;

    @Test
    @DisplayName("circuitBreaker Open시에 Http Status를 4xx번대로 준다")
    void getProductCircuitBreakerOpen() throws Exception {
        // given
        String userId = "userId";
        String productId = "productId";
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("circuitCheckProduct");
        circuitBreaker.transitionToOpenState();

        // then
        assertThat(circuitBreaker.getState()).isEqualByComparingTo(State.OPEN);
        assertThatThrownBy(() -> cartService.add(userId, productId))
                .isInstanceOf(CustomApiException.class)
                .hasMessage(ErrorMessage.INVALID_PRODUCT.getMessage());
    }

}