package hanghae.product_service.controller.req;

public record ProductOrderReqDto(
        String productId,
        int orderCount
) {
}
