package hanghae.user_service;

import hanghae.user_service.service.security.jwt.JwtProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class IntegrationInfraTestSupport {

    @Autowired
    protected JwtProcess jwtProcess;
}
