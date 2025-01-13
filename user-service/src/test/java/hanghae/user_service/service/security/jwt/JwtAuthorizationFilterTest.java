/*
package hanghae.user_service.service.security.jwt;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import hanghae.user_service.domain.user.User;
import hanghae.user_service.domain.user.UserRole;
import hanghae.user_service.testSupport.IntegrationInfraTestSupport;
import java.time.LocalDateTime;
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
        saveUser(UserRole.ROLE_USER);
        String token = jwtProcess.createAccessToken("userId", 1000L);

        // when
        ResultActions resultActions = mockMvc.perform(
                        post("/user/test/123/zz").header(AUTHORIZATION, JwtVO.TOKEN_PREFIX + token))
                .andDo(MockMvcResultHandlers.print());

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("권한이 User인 토큰인 admin으로 가면 forbidden")
    void ForbiddenAuthorization() throws Exception {
        // given
        saveUser(UserRole.ROLE_USER);
        String token = jwtProcess.createAccessToken("userId", 1000L);

        // when
        ResultActions resultActions = mockMvc.perform(
                        post("/admin/test/123/zz").header(AUTHORIZATION, JwtVO.TOKEN_PREFIX + token))
                .andDo(MockMvcResultHandlers.print());

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @DisplayName("JWT가 올바르지 않다면 로그인을 진행하라고 한다")
    void InvalidJWTCallError() throws Exception {
        // given
        saveUser(UserRole.ROLE_USER);
        String token = "testtestToken123afewnfi.oqwhiobrtpnqwiotheiowhrtoipqewnr.oifpnioqwnefioqnweofipnqweniof";

        // when
        ResultActions resultActions = mockMvc.perform(
                        post("/user/test/123/zz").header(AUTHORIZATION, JwtVO.TOKEN_PREFIX + token))
                .andDo(MockMvcResultHandlers.print());

        // then
        resultActions.andExpect(status().isUnauthorized());
    }

    @Nested
    @DisplayName("Admin 권한은 어느곳이나 갈 수 있다")
    class AdminAuthorization {

        @Test
        @DisplayName("admin 권한으로 admin 권한 사이트로 갈 수 있다")
        void AdminToAdminAuthorization() throws Exception {
            // given
            saveUser(UserRole.ROLE_ADMIN);
            String token = jwtProcess.createAccessToken("userId", 1000L);

            // when
            ResultActions resultActions = mockMvc.perform(
                            post("/admin/test/123/zz").header(AUTHORIZATION, JwtVO.TOKEN_PREFIX + token))
                    .andDo(MockMvcResultHandlers.print());

            // then
            resultActions.andExpect(MockMvcResultMatchers.status().isNotFound());
        }

        @Test
        @DisplayName("admin 권한으로 user 권한 사이트로 갈 수 있다")
        void AdminAllOfAuthorization() throws Exception {
            // given
            saveUser(UserRole.ROLE_ADMIN);
            String token = jwtProcess.createAccessToken("userId", 1000L);

            // when
            ResultActions resultActions = mockMvc.perform(
                            post("/user/test/123/zz").header(AUTHORIZATION, JwtVO.TOKEN_PREFIX + token))
                    .andDo(MockMvcResultHandlers.print());

            // then
            resultActions.andExpect(MockMvcResultMatchers.status().isNotFound());
        }
    }

    private void saveUser(UserRole userRole){
        User user = new User(null, "name", "{noop}password", "email@naver.com", userRole, "userId",
                "address", LocalDateTime.now(), LocalDateTime.now());
        userRepository.save(user);
    }


}*/
