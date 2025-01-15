package hanghae.order_service.controller.docs;

import hanghae.order_service.controller.req.OrderCreateReqDto;
import hanghae.order_service.controller.req.OrderFcfsCreateReqDto;
import hanghae.order_service.controller.resp.OrderRespDto;
import hanghae.order_service.controller.resp.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Order Controller", description = "주문 서비스를 위한 API 컨트롤러입니다.")
public interface OrderControllerDocs {

    @Operation(summary = "카트에 있는 상품 주문", description = "카트에 있는 상품을 주문합니다. 사용자 정보와 주소를 포함하여 주문을 처리합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "주문 성공", content = @Content(schema = @Schema(implementation = OrderRespDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @Parameter(name = "address", description = "사용자의 주문 주소")
    ResponseEntity<?> processOrder(String userId, OrderCreateReqDto createReqDto, String address);

    @Operation(summary = "선착순 구매 주문", description = "선착순 구매를 위한 주문을 처리합니다. 구매하기 버튼으로만 가능하며, 주소와 사용자 정보를 포함해야 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "주문 성공", content = @Content(schema = @Schema(implementation = OrderRespDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @Parameter(name = "address", description = "사용자의 주문 주소")
    ResponseEntity<?> processFcfsOrder(String userId, OrderFcfsCreateReqDto createReqDto, String address);

    @Operation(summary = "주문 취소", description = "사용자의 주문을 취소합니다. 주문 ID를 통해 해당 주문을 취소할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "주문 취소 성공"),
            @ApiResponse(responseCode = "404", description = "주문을 찾을 수 없음")
    })
    ResponseEntity<?> cancelOrder(String userId, String orderId);


    @Operation(summary = "주문 환불 요청", description = "주문을 환불 요청합니다. 주문 ID를 통해 해당 주문에 대해 환불을 요청할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "환불 요청 성공"),
            @ApiResponse(responseCode = "404", description = "주문을 찾을 수 없음")
    })
    ResponseEntity<?> prefundOrder(String userId, String orderId);

    @Operation(summary = "사용자 주문 이력 조회", description = "사용자가 완료한 주문 목록을 가져옵니다. 사용자 ID를 통해 주문 내역을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "주문 이력 조회 성공", content = @Content(schema = @Schema(implementation = OrderRespDto.class))),
            @ApiResponse(responseCode = "404", description = "사용자의 주문 내역을 찾을 수 없음")
    })
    ResponseEntity<?> fetchAllUserOrders(String userId);

}
