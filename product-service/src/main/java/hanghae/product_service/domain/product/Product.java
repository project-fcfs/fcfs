package hanghae.product_service.domain.product;

public record Product(
        Long id,
        String name,
        int price,
        int quantity,
        String UUID,
        ProductStatus ProductStatus
) {

    public static Product create(String name, int price, int quantity, String UUID) {
        return new Product(null, name, price, quantity, UUID,
                hanghae.product_service.domain.product.ProductStatus.ACTIVE);
    }
}
