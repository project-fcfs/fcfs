package hanghae.order_service.support;


import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:/application-test.yml")
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class IntegrationResilienceTestSupport {

    @Autowired
    protected CircuitBreakerRegistry circuitBreakerRegistry;

    @Autowired
    protected RetryRegistry retryRegistry;
}
