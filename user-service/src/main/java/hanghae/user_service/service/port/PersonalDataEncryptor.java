package hanghae.user_service.service.port;

public interface PersonalDataEncryptor {
    String encodePassword(String password);

    boolean matchesPassword(String password, String encodedPassword);

    String encodeData(String data);

    String decodeData(String data);
}
