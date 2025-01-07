package hanghae.order_service.controller.req;

public record OrderFcfsCreateReqDto(
        Long productId,
        int orderCount
) {
}
