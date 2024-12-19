package hanghae.product_service.domain.imagefile;

import hanghae.product_service.controller.req.FileInfo;
import hanghae.product_service.domain.product.Product;

public record ImageFileCreateEvent(
        FileInfo fileInfo,
        Product product
) {
}
