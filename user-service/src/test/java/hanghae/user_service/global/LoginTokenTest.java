package hanghae.user_service.global;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import hanghae.user_service.controller.req.UserLoginReqDto;
import hanghae.user_service.domain.user.User;
import hanghae.user_service.service.security.jwt.JwtVO;
import hanghae.user_service.testSupport.IntegrationUserTestSupport;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

public class LoginTokenTest extends IntegrationUserTestSupport {

    @Test
    @DisplayName("로그인에 성공하면 레디스에 토큰이 저장된다")
    void successLoginSaveToken() throws Exception {
        // given
        String email = "123@naver.com";
        String password = "패스워드";
        String encodeEmail = encryptor.encodeData(email);
        userRepository.save(createUser(encodeEmail, "{noop}" + password));
        UserLoginReqDto request = new UserLoginReqDto(email, password);
        String token = "testJwtToken";
        BDDMockito.given(jwtProcess.createAccessToken(any(), anyLong())).willReturn(token);
        String key = env.getProperty("redis.user.token");

        // when
        mockMvc.perform(post("/api/login")
                        .content(om.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());

        Boolean result = redisTemplate.opsForSet().isMember(key, token);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("로그아웃을 하면 레디스에서 토큰이 삭제된다")
    void logoutDeleteToken() throws Exception {
        // given
        String token = "testToken";
        String key = env.getProperty("redis.user.token");
        redisTemplate.opsForSet().add(key, token);

        // when
        mockMvc.perform(post("/logout")
                        .header(HttpHeaders.AUTHORIZATION, JwtVO.TOKEN_PREFIX + token))
                .andDo(MockMvcResultHandlers.print());

        Boolean result = redisTemplate.opsForSet().isMember(key, token);

        // then
        assertThat(result).isFalse();
    }

    private User createUser(String email, String password) {
        return User.normalCreate("name", password, email, "random", "address", LocalDateTime.now());
    }
}
