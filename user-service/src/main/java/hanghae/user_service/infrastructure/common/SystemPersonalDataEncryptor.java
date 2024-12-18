package hanghae.user_service.infrastructure.common;

import hanghae.user_service.service.port.PersonalDataEncryptor;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SystemPersonalDataEncryptor implements PersonalDataEncryptor {


    private final String algorithm;
    private final byte[] secretKey;

    private final PasswordEncoder passwordEncoder;

    public SystemPersonalDataEncryptor(@Value("${my.custom.algorithm}") String algorithm,
                                       @Value("${my.custom.secret-key}")String secretKey,
                                       PasswordEncoder passwordEncoder) {
        this.algorithm = algorithm;
        this.secretKey = secretKey.getBytes();
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public boolean matchesPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public String encodeData(String data) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(secretKey, algorithm);
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encrypted = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            // todo 에러반환
            throw new IllegalStateException("invalid data", e);
        }

    }

    @Override
    public String decodeData(String data) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(secretKey, algorithm);
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decoded = Base64.getDecoder().decode(data);
            return new String(cipher.doFinal(decoded));
        } catch (Exception e) {
            // 에러 반환
            throw new IllegalStateException("invalid data", e);
        }

    }
}
