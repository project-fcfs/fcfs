package hanghae.order_service.infrastructure.product;

public record OrderProductMessage(
        String productId,
        int orderCount
) {
}
