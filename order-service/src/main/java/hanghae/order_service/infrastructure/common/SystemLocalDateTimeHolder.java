package hanghae.order_service.infrastructure.common;

import hanghae.order_service.service.port.LocalDateTimeHolder;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class SystemLocalDateTimeHolder implements LocalDateTimeHolder {
    @Override
    public LocalDateTime getCurrentDate() {
        return LocalDateTime.now();
    }
}
