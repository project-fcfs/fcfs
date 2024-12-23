package hanghae.order_service.domain.product;

public record OrderItem(
        String productId,
        int orderPrice,
        int orderCount
) {
    public OrderItem convertToCartCount(int count){
        return new OrderItem(productId, orderPrice, count);
    }
}
