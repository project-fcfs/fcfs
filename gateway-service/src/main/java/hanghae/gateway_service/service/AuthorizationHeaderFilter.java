package hanghae.gateway_service.service;

import hanghae.gateway_service.service.port.TokenStoreRepository;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    private final JwtTokenProvider provider;
    private final TokenStoreRepository tokenStoreRepository;
    private final Environment env;

    public AuthorizationHeaderFilter(JwtTokenProvider provider, TokenStoreRepository tokenStoreRepository,
                                     Environment env) {
        super(Config.class);
        this.provider = provider;
        this.tokenStoreRepository = tokenStoreRepository;
        this.env = env;
    }

    public static class Config {
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            try {
                String token = provider.resolveToken(request);

                if (token != null && validateToken(token)) {
                    return isLoginUser(token)
                            .flatMap(isLoggedIn -> {
                                if (isLoggedIn) {
                                    String userId = provider.getUserIdFromToken(token);
                                    log.info("user Id -> {} ", userId);

                                    // Request Header에 userId 추가
                                    ServerHttpRequest modifiedRequest = request.mutate()
                                            .header("userId", userId)
                                            .build();

                                    return chain.filter(exchange.mutate().request(modifiedRequest).build());
                                } else {
                                    // 토큰이 유효하지만 로그인하지 않은 경우
                                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                                    return exchange.getResponse().setComplete();
                                }
                            });
                }
            } catch (JwtException e) {
                log.info("JwtException : {}", e.getMessage());
            }

            // 토큰이 없거나 유효하지 않으면 에러 처리
            log.info("JWT Token is invalid or missing");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        };
    }

    private boolean validateToken(String token) {
        return !provider.isExpired(token) && provider.isAccessToken(token);
    }

    private Mono<Boolean> isLoginUser(String token) {
        String key = env.getProperty("redis.user.token");
        return tokenStoreRepository.existLoginToken(key, token);
    }
}
