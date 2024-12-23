package hanghae.order_service.controller.resp;

public record ProductRespDto(
        String name,
        int price,
        int quantity,
        String productId,
        ProductStatus status,
        String imageUrl
) {
    enum ProductStatus {
        ACTIVE("재고 있음"), SOLD_OUT("품절"), INACTIVE("비활성화");

        private final String description;

        ProductStatus(String description) {
            this.description = description;
        }
    }
}
