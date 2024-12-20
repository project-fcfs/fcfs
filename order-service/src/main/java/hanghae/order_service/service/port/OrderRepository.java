package hanghae.order_service.service.port;

import hanghae.order_service.domain.order.Order;

public interface OrderRepository {

    Order save(Order order);

    Order findByUuid(String orderUid);

}
