package hanghae.order_service.domain.order;

import java.time.LocalDateTime;

public record ProductOrder(
        Long id,
        int orderPrice,
        int orderCount,
        String productId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Order order
) {

}
