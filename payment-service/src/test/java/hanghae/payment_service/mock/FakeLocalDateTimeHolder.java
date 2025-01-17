package hanghae.payment_service.mock;


import hanghae.payment_service.service.port.LocalDateTimeHolder;
import java.time.LocalDateTime;

public class FakeLocalDateTimeHolder implements LocalDateTimeHolder {
    private LocalDateTime localDateTime;

    public FakeLocalDateTimeHolder(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    @Override
    public LocalDateTime getCurrentDate() {
        return localDateTime;
    }
}
