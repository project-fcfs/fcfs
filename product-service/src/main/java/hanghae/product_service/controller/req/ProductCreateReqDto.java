package hanghae.product_service.controller.req;

import hanghae.product_service.domain.product.ProductType;

public record ProductCreateReqDto(
        String name,
        int price,
        int quantity,
        ProductType type
) {
}
