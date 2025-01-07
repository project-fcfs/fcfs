package hanghae.order_service.controller.req;

public record CartCreateReqDto(
        Long productId,
        int count
) {
}
