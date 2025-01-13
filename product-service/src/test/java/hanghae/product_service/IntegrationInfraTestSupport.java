package hanghae.product_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

/*
@SpringBootTest
@TestPropertySource(locations = "classpath:/application-test.yml")
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Sql(value = "/delete-all-data.sql")
public class IntegrationInfraTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper mapper;

    @Autowired
    protected RedisTemplate<String, Object> redisTemplate;

    @Autowired
    protected Environment env;
}
*/
