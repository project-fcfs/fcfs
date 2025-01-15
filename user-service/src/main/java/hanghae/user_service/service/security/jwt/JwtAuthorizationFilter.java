package hanghae.user_service.service.security.jwt;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import hanghae.user_service.domain.user.User;
import hanghae.user_service.service.common.exception.ErrorCode;
import hanghae.user_service.service.port.UserRepository;
import hanghae.user_service.service.security.model.LoginUser;
import hanghae.user_service.service.security.model.PrincipalDetails;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private static Logger log = LoggerFactory.getLogger(JwtAuthorizationFilter.class);
    private final JwtProcess jwtProcess;
    private final UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtProcess jwtProcess,
                                  UserRepository userRepository) {
        super(authenticationManager);
        this.jwtProcess = jwtProcess;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (isHeaderVerify(request)) {
            String token = request.getHeader(AUTHORIZATION).replace(JwtVO.TOKEN_PREFIX, "");
            try {
                if (!jwtProcess.isExpired(token)) {
                    String userId = jwtProcess.getUserId(token);
                    User user = userRepository.findByUserId(userId)
                            .orElseThrow(() -> new AccessDeniedException(ErrorCode.INVALID_JWT_TOKEN.getMessage()));

                    PrincipalDetails principalDetails = new PrincipalDetails(LoginUser.create(user));

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            principalDetails, null,
                            List.of(new SimpleGrantedAuthority(user.role().name())));

                    SecurityContextHolder.getContextHolderStrategy().getContext().setAuthentication(authToken);
                }
            } catch (MalformedJwtException | ExpiredJwtException e) {
                log.error("Invalid jwt Token {}", e.getMessage());
            }

        }
        chain.doFilter(request, response);
    }


    private boolean isHeaderVerify(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION);
        return header != null && header.startsWith(JwtVO.TOKEN_PREFIX);
    }
}
