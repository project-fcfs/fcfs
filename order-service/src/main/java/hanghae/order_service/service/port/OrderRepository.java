package hanghae.order_service.service.port;

import hanghae.order_service.domain.order.Order;
import hanghae.order_service.domain.order.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findByUserOrderByOrderId(String userId, String orderId);

    List<Order> findAllRequestRefund(OrderStatus orderStatus, LocalDateTime dateWithMinusDay);

    void saveAll(List<Order> orders);
}
