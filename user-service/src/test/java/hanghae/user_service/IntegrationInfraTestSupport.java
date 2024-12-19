package hanghae.user_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import hanghae.user_service.service.port.UserRepository;
import hanghae.user_service.service.security.jwt.JwtProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Sql(value = "/delete-all-data.sql")
public class IntegrationInfraTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper om;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected JwtProcess jwtProcess;

}
