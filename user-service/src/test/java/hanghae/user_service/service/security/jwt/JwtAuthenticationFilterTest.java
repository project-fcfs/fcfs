/*
package hanghae.user_service.service.security.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import hanghae.user_service.controller.req.UserLoginReqDto;
import hanghae.user_service.domain.user.User;
import hanghae.user_service.service.port.PersonalDataEncryptor;
import hanghae.user_service.testSupport.IntegrationInfraTestSupport;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

class JwtAuthenticationFilterTest extends IntegrationInfraTestSupport {

    @Autowired
    private PersonalDataEncryptor encryptor;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    @DisplayName("DB에 있는 유저와 로그인의 정보가 같으면 JWT를 발급해준다")
    void successfulAuthentication_test() throws Exception {
        // given
        String email = "123@naver.com";
        String password = "패스워드";
        String encodeEmail = encryptor.encodeData(email);
        userRepository.save(createUser(encodeEmail, "{noop}" + password));
        UserLoginReqDto request = new UserLoginReqDto(email, password);

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/login")
                        .content(om.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());

        String token = resultActions.andReturn().getResponse().getHeader(AUTHORIZATION);
        System.out.println("token = " + token);

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Login Success"));
    }

    @Test
    @DisplayName("DB에 있는 회원정보와 로그인 정보가 다르면 에러를 반환한다")
    void unsuccessfulAuthentication_test() throws Exception {
        // given
        String email = "123@naver.com";
        userRepository.save(createUser(email, "{noop}패스워드"));
        UserLoginReqDto request = new UserLoginReqDto(email, "테스트테스트");

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/login")
                        .content(om.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(-1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Login Fail"));
    }

    private User createUser(String email, String password) {
        return User.normalCreate("name", password, email, "random", "address", LocalDateTime.now());
    }

}*/
