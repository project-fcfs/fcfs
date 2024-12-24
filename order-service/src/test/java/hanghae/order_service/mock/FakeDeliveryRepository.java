package hanghae.order_service.mock;

import hanghae.order_service.domain.order.Delivery;
import hanghae.order_service.domain.order.DeliveryStatus;
import hanghae.order_service.service.port.DeliveryRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class FakeDeliveryRepository implements DeliveryRepository {
    private List<Delivery> data = new ArrayList<>();
    private AtomicLong counter = new AtomicLong();

    @Override
    public Delivery save(Delivery input) {
        if (input.id() == null || input.id() == 0L) {
            Delivery newDelivery = new Delivery(counter.incrementAndGet(),
                    input.address(), input.status(), input.createdAt(),
                    input.updatedAt());
            data.add(newDelivery);
            return newDelivery;
        } else {
            data.removeIf(i -> i.id().equals(input.id()));
            data.add(input);
            return input;
        }
    }

    @Override
    public List<Delivery> findDeliveryStatusByDate(DeliveryStatus deliveryStatus, LocalDateTime dateWithMinusDay) {
        return data.stream().filter(i -> i.status().equals(deliveryStatus))
                .filter(i -> !i.updatedAt().isBefore(dateWithMinusDay))
                .toList();
    }

    @Override
    public void saveAll(List<Delivery> results) {
        data.addAll(results);
    }
}
