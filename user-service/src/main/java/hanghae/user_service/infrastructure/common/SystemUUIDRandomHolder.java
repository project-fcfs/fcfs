package hanghae.user_service.infrastructure.common;

import hanghae.user_service.service.port.UUIDRandomHolder;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class SystemUUIDRandomHolder implements UUIDRandomHolder {
    @Override
    public String getRandomUUID() {
        return UUID.randomUUID().toString();
    }
}
