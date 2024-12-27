package hanghae.order_service.controller.resp;

import hanghae.order_service.domain.product.Product;
import hanghae.order_service.domain.product.Product.ProductStatus;

public record ProductRespDto(
        String name,
        Integer price,
        Integer quantity,
        String productId,
        ProductStatus status,
        String imageUrl
) {
    public static ProductRespDto of(Product product){
        return new ProductRespDto(product.name(), product.price(), product.quantity(),
                product.productId(), product.productStatus(), product.imageUrl());
    }
}
