package hanghae.order_service.controller.req;

public record CartUpdateReqDto(
        Long productId,
        int count
) {
}
