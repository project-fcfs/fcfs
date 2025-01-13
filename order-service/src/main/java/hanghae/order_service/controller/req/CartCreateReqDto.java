package hanghae.order_service.controller.req;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "카트에 추가할 상품의 정보를 담고 있는 요청 객체")
public record CartCreateReqDto(
        @Schema(description = "상품 ID", example = "123", required = true)
        Long productId,

        @Schema(description = "상품의 수량", example = "2", required = true)
        int count
) {
}
