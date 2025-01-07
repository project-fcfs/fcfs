package hanghae.order_service.infrastructure.product;

public record OrderMessage(
        Long productId,
        int orderCount
) {
}
