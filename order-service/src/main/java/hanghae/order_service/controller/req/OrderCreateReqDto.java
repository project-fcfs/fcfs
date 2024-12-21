package hanghae.order_service.controller.req;

public record OrderCreateReqDto(
        String productId,
        String address,
        int count
) {
}
