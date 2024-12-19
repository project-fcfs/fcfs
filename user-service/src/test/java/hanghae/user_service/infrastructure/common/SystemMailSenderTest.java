package hanghae.user_service.infrastructure.common;

import static org.junit.jupiter.api.Assertions.*;

import hanghae.user_service.IntegrationInfraTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class SystemMailSenderTest extends IntegrationInfraTestSupport {

    @Autowired
    SystemMailSender mailSender;

    @Test
    @DisplayName("올바른 값을 입력하면 제대로 이메일로 입력을 보낸다")
    void sendAuthCode() throws Exception {
        // given
        String email = "fatum4341@naver.com";
        String value = "테스트입니다. 123";

        // when
        mailSender.sendAuthCode(email, value);

        // then
    }

}