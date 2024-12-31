package hanghae.payment_service.infrastructure.common;


import hanghae.payment_service.service.port.UuidRandomHolder;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class SystemUuidRandomHolder implements UuidRandomHolder {
    @Override
    public String getRandomUuid() {
        return UUID.randomUUID().toString();
    }
}
