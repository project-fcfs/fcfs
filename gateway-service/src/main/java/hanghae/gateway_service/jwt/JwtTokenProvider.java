package hanghae.gateway_service.jwt;

import static hanghae.gateway_service.util.JwtVO.CATEGORY_ACCESS;
import static hanghae.gateway_service.util.JwtVO.HEADER_PREFIX;
import static hanghae.gateway_service.util.JwtVO.PAYLOAD_USER_ID;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;


    public JwtTokenProvider(@Value("${jwt.secret-key}") String secret,
                            @Value("${jwt.algorithm}") String jwtAlgorithm) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), jwtAlgorithm);
    }

    // JWT 토큰에서 userId 추출
    public String getUserIdFromToken(String token) {
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token).getPayload()
                .get(PAYLOAD_USER_ID, String.class);

    }

    // access Token인지 확인
    public boolean isAccessToken(String token) {
        String subject = Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token).getPayload().getSubject();
        return subject.equals(CATEGORY_ACCESS);
    }

    // 토큰 유효성 검사
    public boolean isExpired(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token)
                    .getPayload().getExpiration().before(new Date());
        } catch (JwtException e) {
            return false;
        }
    }

    // Authorization 헤더에서 토큰 추출
    public String resolveToken(ServerHttpRequest request) {
        if (request.getHeaders().containsKey(AUTHORIZATION)) {
            String authToken = request.getHeaders().getFirst(AUTHORIZATION);
            if (authToken.startsWith(HEADER_PREFIX)) {
                return authToken.replace(HEADER_PREFIX, "");
            }
        }
        return null;
    }
}
