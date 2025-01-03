package hanghae.order_service.controller.resp;

import hanghae.order_service.domain.order.Order;
import hanghae.order_service.domain.order.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;

public record OrderRespDto(
        String orderId,
        OrderStatus orderStatus,
        Long amount,
        LocalDateTime updatedAt,
        List<OrderProductRespDto> products
) {

    public static OrderRespDto of(Order order) {
        List<OrderProductRespDto> orderProducts = order.orderProducts().stream()
                .map(OrderProductRespDto::of)
                .toList();
        return new OrderRespDto(order.orderId(), order.orderStatus(), order.getTotalPrice(),order.updatedAt(), orderProducts);
    }
}
