package hanghae.user_service.mock;

import hanghae.user_service.service.port.UUIDRandomHolder;

public class FakeUUIDRandomHolder implements UUIDRandomHolder {
    private String value;

    public void setValue(String value) {
        this.value = value;
    }

    public FakeUUIDRandomHolder(String value) {
        this.value = value;
    }
    @Override
    public String getRandomUUID() {
        return value;
    }
}
