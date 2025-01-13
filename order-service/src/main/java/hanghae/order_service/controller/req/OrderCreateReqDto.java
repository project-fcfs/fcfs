package hanghae.order_service.controller.req;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "주문 생성 시 필요한 상품 ID 목록을 담는 요청 객체")
public record OrderCreateReqDto(
        @Schema(description = "주문할 상품들의 ID 목록", example = "[1, 2, 3]", required = true)
        List<Long> productIds
) {
}
