package hanghae.product_service.controller.req;

import hanghae.product_service.domain.product.ProductType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "상품 생성 요청 DTO")
public record ProductCreateReqDto(
        @Schema(description = "상품 이름", example = "디지털 카메라")
        String name,

        @Schema(description = "상품 가격", example = "500000")
        int price,

        @Schema(description = "상품 수량", example = "100")
        int quantity,

        @Schema(description = "상품 타입", example = "NORMAL", allowableValues = {"NORMAL", "FCFS"})
        ProductType type
) {
}
