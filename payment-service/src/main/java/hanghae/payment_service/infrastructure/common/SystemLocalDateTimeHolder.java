package hanghae.payment_service.infrastructure.common;


import hanghae.payment_service.service.port.LocalDateTimeHolder;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class SystemLocalDateTimeHolder implements LocalDateTimeHolder {
    @Override
    public LocalDateTime getCurrentDate() {
        return LocalDateTime.now();
    }
}
