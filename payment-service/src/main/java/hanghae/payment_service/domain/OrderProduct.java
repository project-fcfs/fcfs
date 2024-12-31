package hanghae.payment_service.domain;

public record OrderProduct(
        int orderPrice,
        int orderCount,
        String productId
) {
}
