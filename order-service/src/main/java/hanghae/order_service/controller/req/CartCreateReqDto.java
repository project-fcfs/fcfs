package hanghae.order_service.controller.req;

public record CartCreateReqDto(
        String productId,
        int count
) {
}
