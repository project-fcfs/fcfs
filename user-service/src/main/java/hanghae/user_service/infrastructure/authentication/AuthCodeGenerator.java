package hanghae.user_service.infrastructure.authentication;

import hanghae.user_service.service.common.exception.CustomApiException;
import hanghae.user_service.service.common.exception.ErrorCode;
import hanghae.user_service.service.port.AuthCodeHolder;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AuthCodeGenerator implements AuthCodeHolder {

    private static final Logger log = LoggerFactory.getLogger(AuthCodeGenerator.class);

    @Override
    public String AuthSixCode() {

        try {
            SecureRandom random = SecureRandom.getInstanceStrong();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 6; i++) {
                int randInt = random.nextInt(10);
                sb.append(randInt);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            log.warn("auth code exception {}", e.getMessage());
            throw new CustomApiException(ErrorCode.INVALID_REQUEST, e);
        }
    }
}
