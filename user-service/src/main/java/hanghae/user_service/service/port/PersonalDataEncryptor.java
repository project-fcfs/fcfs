package hanghae.user_service.service.port;

import java.security.NoSuchAlgorithmException;
import javax.crypto.NoSuchPaddingException;

public interface PersonalDataEncryptor {
    String encodePassword(String password);
    boolean matchesPassword(String password, String encodedPassword);
    String encodeData(String data) throws NoSuchPaddingException, NoSuchAlgorithmException;
    String decodeData(String data);
}
