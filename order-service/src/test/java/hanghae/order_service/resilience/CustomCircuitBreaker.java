package hanghae.order_service.resilience;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CustomCircuitBreaker {

    private static final Logger log = LoggerFactory.getLogger(CustomCircuitBreaker.class);

    @CircuitBreaker(name = "my-circuit-exception", fallbackMethod = "MyCircuitFallback")
    public String myCircuitException(Integer param) {
        log.info("myCircuitException executed {}", param);
        throw new RuntimeException("myCircuitException executed");
    }

    @CircuitBreaker(name = "my-circuit", fallbackMethod = "MyCircuitFallback")
    public String myCircuit(Integer param) {
        log.info("my circuit executed {}", param);
        return param + "executed";
    }

    public String MyCircuitFallback(Integer param, Throwable t) {
        log.info("fall back 호출 {}, param -> {}", t.getMessage(), param);
        return "fallback executed -> " + param;
    }
}
