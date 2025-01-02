package hanghae.payment_service.infrastructure.common;

import hanghae.payment_service.service.port.RandomHolder;
import java.util.Random;
import org.springframework.stereotype.Component;

@Component
public class SystemRandomHolder implements RandomHolder {
    @Override
    public int getRandom() {
        return new Random().nextInt(10) + 1;
    }
}
