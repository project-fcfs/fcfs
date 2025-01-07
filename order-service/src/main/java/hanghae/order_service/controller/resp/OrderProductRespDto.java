package hanghae.order_service.controller.resp;

import hanghae.order_service.domain.order.OrderProduct;
import java.time.LocalDateTime;

public record OrderProductRespDto(
        int orderPrice,
        int orderCount,
        Long productId,
        LocalDateTime createdAt
) {
    public static OrderProductRespDto of(OrderProduct orderProduct) {
        return new OrderProductRespDto(orderProduct.orderPrice(), orderProduct.orderCount(), orderProduct.productId(),
                orderProduct.createdAt());
    }
}
