package hanghae.order_service.infrastructure.product;

import static hanghae.order_service.domain.product.Product.*;

import hanghae.order_service.domain.product.Product;

public record ProductFeignResponse(
        String name,
        Integer price,
        Integer quantity,
        String productId,
        ProductStatus status,
        String imageUrl
) {

    public Product toModel(){
        return new Product(name, price, quantity, productId, status, imageUrl);
    }
}
