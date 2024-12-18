package hanghae.user_service.service.security.jwt;

import static hanghae.user_service.service.security.jwt.JwtVO.CATEGORY_ACCESS;
import static hanghae.user_service.service.security.jwt.JwtVO.CATEGORY_NAME;
import static hanghae.user_service.service.security.jwt.JwtVO.CATEGORY_REFRESH;
import static hanghae.user_service.service.security.jwt.JwtVO.PAYLOAD_EMAIL;
import static hanghae.user_service.service.security.jwt.JwtVO.PAYLOAD_ROLE;
import static hanghae.user_service.service.security.jwt.JwtVO.PAYLOAD_UUID;
import static hanghae.user_service.service.security.jwt.JwtVO.SUBJECT;

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
    private final Long ACCESS_EXPIRATION_TIME;
    private final Long REFRESH_EXPIRATION_TIME;

    public String getEmail(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token)
                .getPayload().get(PAYLOAD_EMAIL, String.class);
    }

    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token)
                .getPayload().get(PAYLOAD_ROLE, String.class);
    }

    public String getUuid(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token)
                .getPayload().get(PAYLOAD_UUID, String.class);
    }

    public boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token)
                .getPayload().getExpiration().before(new Date());
    }

    public JwtProcess(@Value("${jwt.secret-key}") String secret,
                      @Value("${jwt.algorithm}") String jwtAlgorithm,
                      @Value("${jwt.access-time}") Long accessExpirationTime,
                      @Value("${jwt.refresh-time}") Long refreshExpirationTime) {

        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), jwtAlgorithm);
        ACCESS_EXPIRATION_TIME = accessExpirationTime;
        REFRESH_EXPIRATION_TIME = refreshExpirationTime;
    }

    public String createAccessToken(String email, String role, String UUID) {
        return Jwts.builder()
                .subject(SUBJECT)
                .claim(CATEGORY_NAME, CATEGORY_ACCESS)
                .claim(PAYLOAD_EMAIL, email)
                .claim(PAYLOAD_ROLE, role)
                .claim(PAYLOAD_UUID, UUID)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ACCESS_EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }

    public String createRefreshToken() {
        return Jwts.builder()
                .subject(SUBJECT)
                .claim(CATEGORY_NAME, CATEGORY_REFRESH)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }
}
