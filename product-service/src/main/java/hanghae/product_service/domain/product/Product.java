package hanghae.product_service.domain.product;

import hanghae.product_service.service.common.exception.CustomApiException;
import hanghae.product_service.service.common.util.ErrorMessage;

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

    public Product removeStock(int count) {
        int remainingQuantity = quantity - count;
        if(remainingQuantity < 0) {
            throw new CustomApiException(ErrorMessage.OUT_OF_STOCK.getMessage());
        }
        ProductStatus status = (remainingQuantity == 0) ? ProductStatus.SOLD_OUT : ProductStatus.ACTIVE;
        return withUpdatedStock(remainingQuantity, status);
    }

    private Product withUpdatedStock(int remainingQuantity, ProductStatus status) {
        return new Product(this.id, this.name, this.price, remainingQuantity, this.UUID, status);
    }
}
