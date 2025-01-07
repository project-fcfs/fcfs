package hanghae.order_service.infrastructure.product;

import static hanghae.order_service.domain.product.Product.*;

import hanghae.order_service.domain.product.Product;

public record ProductFeignResponse(
        Long productId,
        String name,
        Integer price,
        Integer quantity
) {

    public Product toModel(){
        return new Product(productId, name, price, quantity);
    }
}
