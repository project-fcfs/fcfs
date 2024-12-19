package hanghae.user_service.mock;

import hanghae.user_service.service.port.AuthCodeHolder;

public class FakeAuthCodeHolder implements AuthCodeHolder {
    private String value;

    public FakeAuthCodeHolder(String value) {
        this.value = value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String AuthSixCode() {
        return value;
    }
}
