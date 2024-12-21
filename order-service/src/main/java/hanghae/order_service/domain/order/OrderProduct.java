package hanghae.order_service.domain.order;

import java.time.LocalDateTime;

public record OrderProduct(
        Long id,
        int orderPrice,
        int orderCount,
        String productId,
        LocalDateTime createdAt,
        Order order
) {

    public static OrderProduct create(int orderPrice, int orderCount, String productId, LocalDateTime createdAt) {
        return new OrderProduct(null, orderPrice, orderCount, productId, createdAt, null);
    }

}
