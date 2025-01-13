package hanghae.payment_service.controller;

import hanghae.payment_service.controller.req.PaymentCreateReqDto;
import hanghae.payment_service.domain.Payment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Payment Controller", description = "결제 서비스를 위한 API 컨트롤러입니다.")
public interface PaymentControllerDocs {

    @Parameter(description = "결제 요청 DTO", required = true)
    @Operation(summary = "결제 처리", description = "사용자가 결제 요청을 처리합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "결제가 성공적으로 처리됨",
                    content = @Content(schema = @Schema(implementation = Payment.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    ResponseEntity<?> processPayment(PaymentCreateReqDto reqDto);
}
