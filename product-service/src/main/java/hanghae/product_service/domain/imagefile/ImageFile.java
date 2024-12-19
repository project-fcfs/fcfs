package hanghae.product_service.domain.imagefile;

import hanghae.product_service.domain.product.Product;

public record ImageFile(
        Long id,
        String originalName,
        String storeFileName,
        ImageFileStatus status,
        PhotoType photoType,
        Product product
) {

}
