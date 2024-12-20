package hanghae.order_service.domain.order;

import java.time.LocalDateTime;

public record Delivery(
        Long id,
        DeliveryStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Order order

) {
}
