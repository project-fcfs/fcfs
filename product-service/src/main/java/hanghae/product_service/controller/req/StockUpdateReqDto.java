package hanghae.product_service.controller.req;

public record StockUpdateReqDto(
        Long productId,
        int orderCount
) {
}
