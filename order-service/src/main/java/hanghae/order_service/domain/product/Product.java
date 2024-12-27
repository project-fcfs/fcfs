package hanghae.order_service.domain.product;

public record Product(
        String name,
        int price,
        int quantity,
        String productId,
        ProductStatus productStatus,
        String imageUrl
) {
    public enum ProductStatus {
        ACTIVE("재고 있음"), SOLD_OUT("품절"), INACTIVE("비활성화");

        private final String description;

        ProductStatus(String description) {
            this.description = description;
        }
    }

    public Product convertCart(int quantity){
        return new Product(name, price, quantity, productId, productStatus, imageUrl);
    }
}
