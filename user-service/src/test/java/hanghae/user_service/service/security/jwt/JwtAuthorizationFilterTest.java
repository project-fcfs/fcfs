package hanghae.user_service.service.security.jwt;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import hanghae.user_service.testSupport.IntegrationInfraTestSupport;
import hanghae.user_service.domain.user.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

class JwtAuthorizationFilterTest extends IntegrationInfraTestSupport {

    @Test
    @DisplayName("권한이 User인 토큰이 권한에 맞는 URL로 이동할 시 성공적으로 갈 수 있다")
    void successAuthorization() throws Exception {
        // given
        String token = jwtProcess.createAccessToken("123@naver.com",
                UserRole.ROLE_USER.name(), "random", 1000L);

        // when
        ResultActions resultActions = mockMvc.perform(
                        post("/user/test/123/zz").header(JwtVO.HEADER, JwtVO.TOKEN_PREFIX + token))
                .andDo(MockMvcResultHandlers.print());

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("권한이 User인 토큰인 admin으로 가면 forbidden")
    void ForbiddenAuthorization() throws Exception {
        // given
        String token = jwtProcess.createAccessToken("123@naver.com",
                UserRole.ROLE_USER.name(), "random", 1000L);

        // when
        ResultActions resultActions = mockMvc.perform(
                        post("/admin/test/123/zz").header(JwtVO.HEADER, JwtVO.TOKEN_PREFIX + token))
                .andDo(MockMvcResultHandlers.print());

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @DisplayName("JWT가 올바르지 않다면 에러를 반환한다")
    void InvalidJWTCallError() throws Exception {
        // given
        String token = "testtestToken123afewnfi.oqwhiobrtpnqwiotheiowhrtoipqewnr.oifpnioqwnefioqnweofipnqweniof";

        // when
        ResultActions resultActions = mockMvc.perform(
                        post("/user/test/123/zz").header(JwtVO.HEADER, JwtVO.TOKEN_PREFIX + token))
                .andDo(MockMvcResultHandlers.print());

        // then
        resultActions.andExpect(status().isBadRequest());
    }


    @Nested
    @DisplayName("Admin 권한은 어느곳이나 갈 수 있다")
    class AdminAuthorization {

        @Test
        @DisplayName("admin 권한으로 admin 권한 사이트로 갈 수 있다")
        void AdminToAdminAuthorization() throws Exception {
            // given
            String token = jwtProcess.createAccessToken("123@naver.com",
                    UserRole.ROLE_ADMIN.name(), "random", 1000L);

            // when
            ResultActions resultActions = mockMvc.perform(
                            post("/admin/test/123/zz").header(JwtVO.HEADER, JwtVO.TOKEN_PREFIX + token))
                    .andDo(MockMvcResultHandlers.print());

            // then
            resultActions.andExpect(MockMvcResultMatchers.status().isNotFound());
        }

        @Test
        @DisplayName("admin 권한으로 user 권한 사이트로 갈 수 있다")
        void AdminAllOfAuthorization() throws Exception {
            // given
            String token = jwtProcess.createAccessToken("123@naver.com",
                    UserRole.ROLE_ADMIN.name(), "random", 1000L);

            // when
            ResultActions resultActions = mockMvc.perform(
                            post("/user/test/123/zz").header(JwtVO.HEADER, JwtVO.TOKEN_PREFIX + token))
                    .andDo(MockMvcResultHandlers.print());

            // then
            resultActions.andExpect(MockMvcResultMatchers.status().isNotFound());
        }
    }


}