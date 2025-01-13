package hanghae.payment_service.controller.req;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "결제 요청 DTO")
public record PaymentCreateReqDto(
        @Schema(description = "주문 ID", required = true)
        String orderId,

        @Schema(description = "결제 금액", required = true)
        Long amount
) {
}
