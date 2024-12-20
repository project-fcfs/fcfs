package hanghae.order_service.infrastructure.order;

import hanghae.order_service.domain.order.OrderStatus;
import java.time.LocalDateTime;

public class OrderEntity {
    private Long id;
    private Long totalPrice;
    private OrderStatus orderStatus;
    private String userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
