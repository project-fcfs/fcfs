package hanghae.product_service.controller.resp;

import hanghae.product_service.domain.product.Product;

public record ProductRespDto(
        String name,
        int price,
        int quantity,
        String imageUrl
) {
    public static ProductRespDto of(Product product, String storeFileName){
        return new ProductRespDto(
                product.name(), product.price(), product.quantity(), storeFileName
        );
    }
}
