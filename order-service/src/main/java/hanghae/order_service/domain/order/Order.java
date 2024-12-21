package hanghae.order_service.domain.order;

import java.time.LocalDateTime;
import java.util.List;

public record Order(
        Long id,
        OrderStatus orderStatus,
        String userId,
        String uuid,
        List<OrderProduct> orderProducts,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static Order create(String userId, String uuid, List<OrderProduct> orderProducts
                               ,LocalDateTime currentDate) {
        return new Order(null, OrderStatus.PENDING, userId, uuid,
                orderProducts, currentDate, currentDate);
    }

}
