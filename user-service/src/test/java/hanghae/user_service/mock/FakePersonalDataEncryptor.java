package hanghae.user_service.mock;

import hanghae.user_service.service.port.PersonalDataEncryptor;

public class FakePersonalDataEncryptor implements PersonalDataEncryptor {
    private final String encrypt;

    public FakePersonalDataEncryptor(String encrypt) {
        this.encrypt = encrypt;
    }

    @Override
    public String encodePassword(String password) {
        return encrypt + password;
    }

    @Override
    public boolean matchesPassword(String password, String encodedPassword) {
        return (encrypt + password).equals(encodedPassword);
    }

    @Override
    public String encodeData(String data) {
        return encrypt + data;
    }

    @Override
    public String decodeData(String data) {
        return data.replace(encrypt, "");
    }
}
