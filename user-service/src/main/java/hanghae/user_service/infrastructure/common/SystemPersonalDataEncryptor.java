package hanghae.user_service.infrastructure.common;

import hanghae.user_service.service.port.PersonalDataEncryptor;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SystemPersonalDataEncryptor implements PersonalDataEncryptor {

    private static final Charset ENCODING_TYPE = StandardCharsets.UTF_8;
    private final PasswordEncoder passwordEncoder;
    private final AesBytesEncryptor encryptor;

    public SystemPersonalDataEncryptor(PasswordEncoder passwordEncoder,
                                       AesBytesEncryptor encryptor) {
        this.passwordEncoder = passwordEncoder;
        this.encryptor = encryptor;
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
        byte[] encrypt = encryptor.encrypt(data.getBytes(ENCODING_TYPE));
        return Base64.getEncoder().encodeToString(encrypt);
    }

    @Override
    public String decodeData(String data) {
        byte[] decode = Base64.getDecoder().decode(data);
        byte[] decrypt = encryptor.decrypt(decode);
        return new String(decrypt, ENCODING_TYPE);
    }
}
