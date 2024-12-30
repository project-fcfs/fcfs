package hanghae.order_service.resilience;

import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CustomRetry {

    private static final Logger log = LoggerFactory.getLogger(CustomCircuitBreaker.class);

    @Retry(name = "my-retry-exception", fallbackMethod = "MyRetryFallback")
    public String myRetryException(Integer param) {
        log.info("myRetryException executed {}", param);
        throw new RuntimeException("myRetryException executed");
    }

    @Retry(name = "my-retry", fallbackMethod = "MyRetryFallback")
    public String myRetry(Integer param) {
        log.info("my retry executed {}", param);
        return param + "executed";
    }

    public String MyRetryFallback(Integer param, Throwable t) {
        log.info("fall back 호출 {}, param -> {}", t.getMessage(), param);
        return "fallback executed -> " + param;
    }
}
