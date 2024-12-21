package hanghae.order_service.service.port;

import hanghae.order_service.domain.order.OrderProduct;

public interface OrderProductRepository {
    void save(OrderProduct orderProduct);
}
