package hanghae.product_service.domain.product;

public record Product(
        Long id,
        String name,
        int price,
        int quantity,
        String UUID,
        ProductStatus productStatus
) {

    public static Product create(String name, int price, int quantity, String UUID) {
        return new Product(null, name, price, quantity, UUID,
                ProductStatus.ACTIVE);
    }
}
