package hanghae.order_service.service.port;

import hanghae.order_service.domain.order.Order;
import java.util.Optional;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findByUserOrderByOrderId(String userId, String orderId);

}
