package hanghae.order_service.service.port;

import hanghae.order_service.domain.order.Delivery;

public interface DeliveryRepository {
    void save(Delivery delivery);
}
