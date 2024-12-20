package hanghae.order_service.domain.order;

import java.time.LocalDateTime;

public record Order(
        Long id,
        Long totalPrice,
        OrderStatus orderStatus,
        String userId,
        String uuid,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

}
