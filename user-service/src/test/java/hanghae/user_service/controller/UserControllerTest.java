/*
package hanghae.user_service.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import hanghae.user_service.controller.req.UserAuthCodeReqDto;
import hanghae.user_service.controller.req.UserCreateReqDto;
import hanghae.user_service.domain.user.User;
import hanghae.user_service.domain.user.UserRole;
import hanghae.user_service.service.AuthenticationService;
import hanghae.user_service.service.port.MailSenderHolder;
import hanghae.user_service.testSupport.IntegrationInfraTestSupport;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.ResultActions;

class UserControllerTest extends IntegrationInfraTestSupport {

    @MockitoBean
    MailSenderHolder senderHolder;
    @MockitoBean
    AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        User user = new User(null, "name", "{noop}password", "email@naver.com", UserRole.ROLE_USER, "userId",
                "address", LocalDateTime.now(), LocalDateTime.now());
        userRepository.save(user);
    }

    @Test
    @DisplayName("인증코드를 요청하면 인증번호를 받을 수 있다")
    void requestCode() throws Exception {
        // given
        UserAuthCodeReqDto request = new UserAuthCodeReqDto("fatum4341@naver.com");

        // when
        ResultActions resultActions = mockMvc.perform(post("/authcode").content(om.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("올바른 값을 입력하면 유저를 저장할 수 있다")
    void createUser() throws Exception {
        // given
        UserCreateReqDto request = new UserCreateReqDto("123@naver.com", "내이름은",
                "password", "address", "123456");

        // when
        ResultActions resultActions = mockMvc.perform(post("/signup").content(om.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print());

        // then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("내이름은"));
    }

    @Nested
    @DisplayName("유효성 검사")
    class InvalidBinding {

        @Test
        @DisplayName("")
        void 이름_test() throws Exception {
            // given

            // when

            // then
        }
    }

}*/
