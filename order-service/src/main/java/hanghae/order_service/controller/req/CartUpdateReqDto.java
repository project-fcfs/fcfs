package hanghae.order_service.controller.req;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "카트에 있는 상품의 수량을 업데이트할 때 사용하는 요청 객체")
public record CartUpdateReqDto(
        @Schema(description = "수량을 변경할 상품의 ID", example = "123", required = true)
        Long productId,

        @Schema(description = "상품의 새로운 수량", example = "2", required = true)
        int count
) {
}
