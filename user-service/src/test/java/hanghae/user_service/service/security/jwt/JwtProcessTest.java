package hanghae.user_service.service.security.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import hanghae.user_service.IntegrationInfraTestSupport;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class JwtProcessTest extends IntegrationInfraTestSupport {

    @Autowired
    protected JwtProcess jwtProcess;

    @Test
    @DisplayName("올바른 값을 입력하면 JWT 토큰을 생성할 수 있다")
    void createJwt() throws Exception {
        // given
        String email = "email@email.com";
        String role = "ROLE_USER";

        // when
        String result = jwtProcess.createAccessToken(email, role, "random", 100L);

        // then
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("토큰의 유효기간을 넘으면 에러를 반환한다")
    void verifyToken() throws Exception {
        // given
        String result = jwtProcess.createAccessToken("hi@naver.com", "ROLE_USER", "random",0L);

        // then
        assertThatThrownBy(() -> jwtProcess.isExpired(result))
                .isInstanceOf(ExpiredJwtException.class);
    }

    @Test
    @DisplayName("생성한 토큰에 올바른 값을 빼낼 수 있다")
    void canExtractToken() throws Exception {
        // given
        String inputEmail = "email@email.com";
        String inputRole = "ROLE_USER";
        String inputUuid = "random";

        String token = jwtProcess.createAccessToken(inputEmail, inputRole, inputUuid, 1000L);

        // when
        String role = jwtProcess.getRole(token);
        String email = jwtProcess.getEmail(token);
        String uuid = jwtProcess.getUuid(token);

        // then
        assertAll(() -> {
            assertThat(role).isEqualTo(inputRole);
            assertThat(email).isEqualTo(inputEmail);
            assertThat(uuid).isEqualTo(inputUuid);
        });
    }

}