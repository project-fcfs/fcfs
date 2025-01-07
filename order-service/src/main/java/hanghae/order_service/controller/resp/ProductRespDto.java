package hanghae.order_service.controller.resp;

import hanghae.order_service.domain.product.Product;

public record ProductRespDto(
        String name,
        Integer price,
        Integer quantity,
        Long productId
) {
    public static ProductRespDto of(Product product) {
        return new ProductRespDto(product.name(), product.price(), product.quantity(),
                product.productId());
    }

}
