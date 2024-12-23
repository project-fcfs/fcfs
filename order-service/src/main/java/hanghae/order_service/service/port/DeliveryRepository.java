package hanghae.order_service.service.port;

import hanghae.order_service.domain.order.Delivery;
import hanghae.order_service.domain.order.DeliveryStatus;
import java.time.LocalDateTime;
import java.util.List;

public interface DeliveryRepository {
    Delivery save(Delivery delivery);

    List<Delivery> findDeliveryStatusByDate(DeliveryStatus deliveryStatus, LocalDateTime dateWithMinusDay);

    void saveAll(List<Delivery> results);
}
