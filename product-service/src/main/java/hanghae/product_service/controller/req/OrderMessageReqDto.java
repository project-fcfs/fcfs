package hanghae.product_service.controller.req;

public record OrderMessageReqDto(
        String productId,
        int orderCount,
        String orderId
) {
}
