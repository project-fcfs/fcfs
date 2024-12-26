package hanghae.gateway_service.filter;

import hanghae.gateway_service.jwt.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    private final JwtTokenProvider provider;

    public AuthorizationHeaderFilter(JwtTokenProvider provider) {
        super(Config.class);
        this.provider = provider;
    }

    public static class Config {
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            try{
                String token = provider.resolveToken(request);

                if (token != null && !validateToken(token)) {
                    String userId = provider.getUserIdFromToken(token);
                    log.info("user Id -> {} ", userId);

                    // 3. Request Header에 userId 추가
                    ServerHttpRequest modifiedRequest = request.mutate()
                            .header("userId", userId)
                            .build();

                    return chain.filter(exchange.mutate().request(modifiedRequest).build());
                }
            } catch (JwtException e) {
                log.info("JwtException : {}", e.getMessage());
            }

            // 토큰이 없거나 유효하지 않으면 에러 처리
            log.info("JWT Token is invalid or missing");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        });
    }


    private boolean validateToken(String token) {
        return provider.isExpired(token) || !provider.isAccessToken(token);
    }
}
