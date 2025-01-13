package hanghae.order_service.controller.req;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "선착순 구매 주문 생성 요청 객체")
public record OrderFcfsCreateReqDto(
        @Schema(description = "선착순 구매할 상품의 ID", example = "1", required = true)
        Long productId,

        @Schema(description = "선착순 구매할 상품의 수량", example = "2", required = true)
        int orderCount
) {
}
