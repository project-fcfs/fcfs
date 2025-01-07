package hanghae.order_service.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:/application-test.yml")
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class IntegrationTestSupport{

    @Autowired
    protected RedisTemplate<String, Object> redisTemplate;

    @Autowired
    protected ObjectMapper mapper;

    @Autowired
    protected Environment env;
}
