package hanghae.order_service.controller.req;

public record OrderFcfsCreateReqDto(
        String productId,
        int orderCount
) {
}
