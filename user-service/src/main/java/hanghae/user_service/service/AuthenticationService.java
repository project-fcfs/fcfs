package hanghae.user_service.service;

import static hanghae.user_service.service.common.util.UserConstant.*;

import hanghae.user_service.service.common.exception.CustomApiException;
import hanghae.user_service.service.common.util.ErrorMessage;
import hanghae.user_service.service.common.util.UserConstant;
import hanghae.user_service.service.port.AuthCodeHolder;
import hanghae.user_service.service.port.AuthenticationRepository;
import hanghae.user_service.service.port.MailSenderHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private static final Long AUTH_CODE_EXPIRATION = 1000 * 5L;
    private final AuthCodeHolder authCodeHolder;
    private final AuthenticationRepository authenticationRepository;
    private final MailSenderHolder mailSenderHolder;

    public AuthenticationService(AuthCodeHolder authCodeHolder, AuthenticationRepository authenticationRepository,
                                 MailSenderHolder mailSenderHolder) {
        this.authCodeHolder = authCodeHolder;
        this.authenticationRepository = authenticationRepository;
        this.mailSenderHolder = mailSenderHolder;
    }

    public void send(String email) {
        String authCode = authCodeHolder.AuthSixCode();
        String key = generateKey(email);
        authenticationRepository.save(key, authCode, AUTH_CODE_EXPIRATION);
        mailSenderHolder.sendAuthCode(email,authCode);
    }

    public void verifyCode(String email, String authCode) {
        String key = generateKey(email);
        if (!authenticationRepository.existsCode(key, authCode)) {
            throw new CustomApiException(ErrorMessage.INVALID_AUTH_TOKEN.getMessage());
        }
    }

    private String generateKey(String email) {
        return AUTH_PREFIX + email;
    }
}
