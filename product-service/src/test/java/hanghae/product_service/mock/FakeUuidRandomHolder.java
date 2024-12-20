package hanghae.product_service.mock;

import hanghae.product_service.service.port.UUIDRandomHolder;

public class FakeUuidRandomHolder implements UUIDRandomHolder {
    private String value;

    public void setValue(String value) {
        this.value = value;
    }

    public FakeUuidRandomHolder(String value) {
        this.value = value;
    }

    @Override
    public String getRandomUUID() {
        return value;
    }
}
