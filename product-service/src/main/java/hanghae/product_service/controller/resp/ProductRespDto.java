package hanghae.product_service.controller.resp;

import hanghae.product_service.domain.imagefile.ImageFile;
import hanghae.product_service.domain.product.Product;
import hanghae.product_service.domain.product.ProductStatus;

public record ProductRespDto(
        String name,
        int price,
        int quantity,
        ProductStatus status,
        String imageUrl
) {
    public static ProductRespDto of(Product product, ImageFile imageFile){
        return new ProductRespDto(
                product.name(), product.price(), product.quantity(),product.productStatus(),
                imageFile == null ? null : imageFile.storeFileName()
        );
    }
}
