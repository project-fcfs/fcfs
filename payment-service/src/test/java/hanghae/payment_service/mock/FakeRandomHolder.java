package hanghae.payment_service.mock;

import hanghae.payment_service.service.port.RandomHolder;

public class FakeRandomHolder implements RandomHolder {
    private int value;

    public FakeRandomHolder(int value) {
        this.value = value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public int getRandom() {
        return value;
    }
}
