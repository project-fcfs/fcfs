package hanghae.product_service.controller.req;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "상품 수량 업데이트 요청 DTO")
public record StockUpdateReqDto(
        @Schema(description = "상품 ID", example = "1")
        Long productId,

        @Schema(description = "주문 수량", example = "5")
        int orderCount
) {
}
