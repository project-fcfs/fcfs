package hanghae.user_service.service.security.handler;

import hanghae.user_service.service.common.util.CustomResponseUtil;
import hanghae.user_service.service.common.util.ErrorMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

public class FormAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        CustomResponseUtil.fail(response, ErrorMessage.ERROR_ACCESS_DENIED.getMessage(), HttpStatus.FORBIDDEN);
    }
}
