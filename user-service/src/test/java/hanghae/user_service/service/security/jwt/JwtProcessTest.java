package hanghae.user_service.service.security.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import hanghae.user_service.testSupport.IntegrationInfraTestSupport;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JwtProcessTest extends IntegrationInfraTestSupport {

    @Test
    @DisplayName("올바른 값을 입력하면 JWT 토큰을 생성할 수 있다")
    void createJwt() throws Exception {
        // given
        String email = "email@email.com";
        String role = "ROLE_USER";

        // when
        String result = jwtProcess.createAccessToken("userId", 100L);

        // then
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("토큰의 유효기간을 넘으면 에러를 반환한다")
    void verifyToken() throws Exception {
        // given
        String result = jwtProcess.createAccessToken("userId", 0L);

        // then
        assertThatThrownBy(() -> jwtProcess.isExpired(result))
                .isInstanceOf(ExpiredJwtException.class);
    }

    @Test
    @DisplayName("생성한 토큰에 올바른 값을 빼낼 수 있다")
    void canExtractToken() throws Exception {
        // given
        String userId = "random";

        String token = jwtProcess.createAccessToken(userId, 1000L);

        // when
        String uuid = jwtProcess.getUserId(token);

        // then
        assertAll(() -> {
            assertThat(uuid).isEqualTo(userId);
        });
    }

}