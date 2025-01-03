package hanghae.order_service.controller.req;

public record CartUpdateReqDto(
        String productId,
        int count
) {
}
