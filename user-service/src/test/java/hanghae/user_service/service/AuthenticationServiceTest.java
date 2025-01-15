package hanghae.user_service.service;

import static hanghae.user_service.service.common.util.UserConstant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import hanghae.user_service.mock.FakeAuthCodeHolder;
import hanghae.user_service.mock.FakeAuthenticationRepository;
import hanghae.user_service.mock.FakeMailSenderHolder;
import hanghae.user_service.service.common.exception.CustomApiException;
import hanghae.user_service.service.common.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthenticationServiceTest {

    private AuthenticationService authenticationService;
    private FakeAuthCodeHolder authCodeHolder;
    private FakeMailSenderHolder mailSenderHolder;
    private FakeAuthenticationRepository authenticationRepository;

    @BeforeEach
    void setUp() {
        authCodeHolder = new FakeAuthCodeHolder("authCode");
        authenticationRepository = new FakeAuthenticationRepository();
        mailSenderHolder = new FakeMailSenderHolder();
        authenticationService = new AuthenticationService(authCodeHolder, authenticationRepository, mailSenderHolder);
    }

    @Test
    @DisplayName("이메일을 주면 올바른 값으로 이메일로 인증코드를 보낼 수 있다")
    void auth_code_test() throws Exception {
        // given
        String email = "test@test.com";

        // when
        authenticationService.send(email);
        boolean result = mailSenderHolder.isSendAuthCode(email);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("인증코드가 올바르면 정상적으로 작동한다")
    void verifyCode() throws Exception {
        // given
        String email = "test@test.com";
        String authCode = "testCode";
        authCodeHolder.setValue(authCode);
        authenticationService.send(email);

        // when
        authenticationService.verifyCode(email, authCode);
        boolean result = authenticationRepository.existsCode(AUTH_PREFIX + email, authCode);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("인증코드가 올바르지 않다면 에러를 반환한다")
    void InvalidVerifyCodeWithError() throws Exception {
        // given
        String email = "test@test.com";
        authenticationService.send(email);

        // then
        assertThatThrownBy(() -> authenticationService.verifyCode(email, "test"))
                .isInstanceOf(CustomApiException.class)
                .hasMessage(ErrorCode.INVALID_AUTH_TOKEN.getMessage());
    }
}