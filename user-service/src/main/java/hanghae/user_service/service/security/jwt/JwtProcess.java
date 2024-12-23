package hanghae.user_service.service.security.jwt;

import static hanghae.user_service.service.security.jwt.JwtVO.CATEGORY_ACCESS;
import static hanghae.user_service.service.security.jwt.JwtVO.CATEGORY_REFRESH;
import static hanghae.user_service.service.security.jwt.JwtVO.PAYLOAD_USER_ID;

import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProcess {

    private final SecretKey secretKey;


    public JwtProcess(@Value("${jwt.secret-key}") String secret,
                      @Value("${jwt.algorithm}") String jwtAlgorithm) {

        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), jwtAlgorithm);
    }

    public String getUserId(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token)
                .getPayload().get(PAYLOAD_USER_ID, String.class);
    }

    public boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token)
                .getPayload().getExpiration().before(new Date());
    }

    public String createAccessToken(String userId, Long expireTime) {
        return Jwts.builder()
                .subject(CATEGORY_ACCESS)
                .claim(PAYLOAD_USER_ID, userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(secretKey)
                .compact();
    }

    public String createRefreshToken(String userId, Long expireTime) {
        return Jwts.builder()
                .subject(CATEGORY_REFRESH)
                .claim(PAYLOAD_USER_ID, CATEGORY_REFRESH)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(secretKey)
                .compact();
    }
}
