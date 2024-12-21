package hanghae.order_service.infrastructure.order;

import hanghae.order_service.domain.order.OrderStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long totalPrice;
    private OrderStatus orderStatus;
    private String userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
