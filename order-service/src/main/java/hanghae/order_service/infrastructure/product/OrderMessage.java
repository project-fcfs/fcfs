package hanghae.order_service.infrastructure.product;

public record OrderMessage(
        String productId,
        int orderCount,
        String orderId
) {
}
