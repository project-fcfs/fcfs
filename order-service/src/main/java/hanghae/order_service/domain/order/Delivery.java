package hanghae.order_service.domain.order;

import java.time.LocalDateTime;

public record Delivery(
        Long id,
        String address,
        DeliveryStatus status,
        Order order,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static Delivery create(String address, Order order,LocalDateTime currentDate) {
        return new Delivery(null, address, DeliveryStatus.PREPARING, order,currentDate, currentDate);
    }
}
