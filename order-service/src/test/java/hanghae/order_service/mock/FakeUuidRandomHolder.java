package hanghae.order_service.mock;

import hanghae.order_service.service.port.UuidRandomHolder;

public class FakeUuidRandomHolder implements UuidRandomHolder {
    private String value;

    public FakeUuidRandomHolder(String value) {
        this.value = value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getRandomUuid() {
        return value;
    }
}
