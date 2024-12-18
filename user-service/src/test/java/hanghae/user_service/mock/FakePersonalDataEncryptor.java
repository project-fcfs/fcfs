package hanghae.user_service.mock;

import hanghae.user_service.service.port.PersonalDataEncryptor;

public class FakePersonalDataEncryptor implements PersonalDataEncryptor {
    private static final String PASSWORD_ENCRYPT = "password_";
    private static final String DATA_ENCRYPT = "data_";

    @Override
    public String encodePassword(String password) {
        return PASSWORD_ENCRYPT + password;
    }

    @Override
    public boolean matchesPassword(String password, String encodedPassword) {
        return (PASSWORD_ENCRYPT + password).equals(encodedPassword);
    }

    @Override
    public String encodeData(String data) {
        return DATA_ENCRYPT + data;
    }

    @Override
    public String decodeData(String data) {
        return data.replace(PASSWORD_ENCRYPT, "");
    }
}
