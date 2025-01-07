package hanghae.order_service.domain.product;

public record Product(
        Long productId,
        String name,
        int price,
        int quantity
) {

    public Product convertCart(int quantity){
        return new Product(productId,name, price, quantity);
    }
}
