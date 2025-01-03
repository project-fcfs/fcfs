package hanghae.product_service.controller.req;

public record OrderCreateReqDto(
        String productId,
        Integer orderCount
) {
}
