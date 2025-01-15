package hanghae.product_service.domain.product;

import static hanghae.product_service.service.common.util.ProductConstant.SHOW_PRODUCT_ID;

import hanghae.product_service.service.common.exception.CustomApiException;
import hanghae.product_service.service.common.exception.ErrorCode;

public record Product(
        Long id,
        String name,
        int price,
        int quantity,
        ProductType type,
        ProductStatus status
) {

    public static Product create(String name, int price, int quantity, ProductType type) {
        return new Product(null, name, price, quantity,
                type, ProductStatus.ACTIVE);
    }

    public Product removeStock(int count) {
        int remainingQuantity = quantity - count;
        if (remainingQuantity < 0) {
            throw new CustomApiException(ErrorCode.OUT_OF_STOCK, SHOW_PRODUCT_ID + id);
        }
        ProductStatus status = (remainingQuantity == 0) ? ProductStatus.SOLD_OUT : ProductStatus.ACTIVE;
        return withUpdatedStock(remainingQuantity, status);
    }

    public Product addStock(int count) {
        int remainingQuantity = quantity + count;
        ProductStatus status = this.status;
        if (quantity == 0) {
            status = ProductStatus.ACTIVE;
        }
        return withUpdatedStock(remainingQuantity, status);
    }

    private Product withUpdatedStock(int remainingQuantity, ProductStatus status) {
        return new Product(id, name, price, remainingQuantity, type, status);
    }
}
