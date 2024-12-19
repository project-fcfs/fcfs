package hanghae.user_service.service.security.handler;

import hanghae.user_service.service.common.util.CustomResponseUtil;
import hanghae.user_service.service.common.util.ErrorMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class FormEntryPointHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        String message = ErrorMessage.MUST_LOGIN_REQUIRED.getMessage();

        CustomResponseUtil.fail(response, message, HttpStatus.UNAUTHORIZED);
    }
}
