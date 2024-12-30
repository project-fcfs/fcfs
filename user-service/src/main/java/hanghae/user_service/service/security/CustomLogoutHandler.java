package hanghae.user_service.service.security;

import hanghae.user_service.service.port.TokenStoreRepository;
import hanghae.user_service.service.security.jwt.JwtVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.http.HttpHeaders;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;

public class CustomLogoutHandler implements LogoutHandler {
    private final TokenStoreRepository tokenStoreRepository;
    private final Environment env;

    public CustomLogoutHandler(TokenStoreRepository tokenStoreRepository, Environment env) {
        this.tokenStoreRepository = tokenStoreRepository;
        this.env = env;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        request.getSession().invalidate();
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(authorization != null && authorization.startsWith(JwtVO.TOKEN_PREFIX)) {
            String token = authorization.replace(JwtVO.TOKEN_PREFIX, "");
            String key = env.getProperty("redis.user.token");
            tokenStoreRepository.deleteToken(key, token);
        }
        SecurityContextHolder.getContextHolderStrategy().getContext().setAuthentication(null);
        SecurityContextHolder.getContextHolderStrategy().clearContext();
    }

}
