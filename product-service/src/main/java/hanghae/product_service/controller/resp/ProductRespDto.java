package hanghae.product_service.controller.resp;

import hanghae.product_service.domain.imagefile.ImageFile;
import hanghae.product_service.domain.product.Product;
import hanghae.product_service.domain.product.ProductStatus;
import hanghae.product_service.domain.product.ProductType;

public record ProductRespDto(
        Long id,
        String name,
        int price,
        int quantity,
        ProductType type,
        ProductStatus status,
        String imageUrl
) {
    public static ProductRespDto of(Product product, ImageFile imageFile) {
        return new ProductRespDto(
                product.id(), product.name(), product.price(), product.quantity(),
                product.type(), product.status(),
                imageFile == null ? null : imageFile.storeFileName()
        );
    }
}
