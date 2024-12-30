package hanghae.user_service.service.security;

import hanghae.user_service.service.port.TokenStoreRepository;
import hanghae.user_service.service.security.jwt.JwtVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;

public class CustomLogoutHandler implements LogoutHandler {
    private final TokenStoreRepository tokenStoreRepository;

    public CustomLogoutHandler(TokenStoreRepository tokenStoreRepository) {
        this.tokenStoreRepository = tokenStoreRepository;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        request.getSession().invalidate();
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(authorization != null && authorization.startsWith(JwtVO.TOKEN_PREFIX)) {
            String token = authorization.replace(JwtVO.TOKEN_PREFIX, "");
            tokenStoreRepository.deleteToken(token);
        }
        response.setHeader(HttpHeaders.AUTHORIZATION, "");
        SecurityContextHolder.getContextHolderStrategy().getContext().setAuthentication(null);
        SecurityContextHolder.getContextHolderStrategy().clearContext();
    }

}
