package hanghae.order_service.controller.req;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "카트에서 삭제할 상품의 정보를 담고 있는 요청 객체")
public record CartDeleteReqDto(
        @Schema(description = "삭제할 상품의 ID", example = "123", required = true)
        Long productId
) {
}
