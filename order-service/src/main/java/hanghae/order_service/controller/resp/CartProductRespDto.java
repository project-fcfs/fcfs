package hanghae.order_service.controller.resp;

import hanghae.order_service.controller.resp.ProductRespDto.ProductStatus;

public record CartProductRespDto(
        String name,
        int price,
        int quantity,
        String productId,
        ProductStatus status,
        String imageUrl
) {
    public static CartProductRespDto of(ProductRespDto product, int quantity) {
        return new CartProductRespDto(product.name(),product.price(),
                quantity, product.productId(), product.status(), product.imageUrl());
    }
}
