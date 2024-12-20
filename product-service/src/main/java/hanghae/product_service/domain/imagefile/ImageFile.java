package hanghae.product_service.domain.imagefile;

import hanghae.product_service.domain.product.Product;

public record ImageFile(
        Long id,
        String originalName,
        String storeFileName,
        ImageFileStatus status,
        Product product
) {

    public static ImageFile create(String originalName, String storeFileName, Product product) {
        return new ImageFile(null, originalName, storeFileName,
                ImageFileStatus.ACTIVE, product);
    }

}
