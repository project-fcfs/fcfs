package hanghae.order_service.domain.order;

import java.time.LocalDateTime;
import java.util.List;

public record Order(
        Long id,
        OrderStatus orderStatus,
        String userId,
        String uuid,
        List<OrderProduct> orderProducts,
        Delivery delivery,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static Order create(String userId, String uuid, List<OrderProduct> orderProducts,
                               Delivery delivery,LocalDateTime currentDate) {
        return new Order(null, OrderStatus.PENDING, userId, uuid,
                orderProducts, delivery, currentDate, currentDate);
    }

    public Order cancelOrder(LocalDateTime currentDate){
        Delivery canceldDelivery = delivery.cancelIfPossible(currentDate);
        return new Order(id, OrderStatus.CANCELLED, userId, uuid, orderProducts,
                canceldDelivery, createdAt, currentDate);
    }

    public Order processRefund(LocalDateTime currentDate){
        Delivery returnDelivery = delivery.refundIfEligible(currentDate);
        return new Order(id, OrderStatus.CANCELLED, userId, uuid, orderProducts,
                returnDelivery, createdAt, currentDate);
    }

    public Order handlerOutOfStock(LocalDateTime currentDate){
        Delivery canceldDelivery = delivery.cancelOutOfStock(currentDate);
        return new Order(id, OrderStatus.CANCELLED, userId, uuid, orderProducts,
                canceldDelivery, createdAt, currentDate);
    }

    public int getTotalPrice(){
        return orderProducts.stream()
                .mapToInt(OrderProduct::getTotalPrice)
                .sum();
    }

}
