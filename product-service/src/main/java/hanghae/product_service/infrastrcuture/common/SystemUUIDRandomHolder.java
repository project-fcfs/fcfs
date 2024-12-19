package hanghae.product_service.infrastrcuture.common;

import hanghae.product_service.service.port.UUIDRandomHolder;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class SystemUUIDRandomHolder implements UUIDRandomHolder {
    @Override
    public String getRandomUUID() {
        return UUID.randomUUID().toString();
    }
}
