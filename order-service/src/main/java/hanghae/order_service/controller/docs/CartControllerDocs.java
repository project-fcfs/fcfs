package hanghae.order_service.controller.docs;

import hanghae.order_service.controller.req.CartCreateReqDto;
import hanghae.order_service.controller.req.CartDeleteReqDto;
import hanghae.order_service.controller.req.CartUpdateReqDto;
import hanghae.order_service.controller.resp.ProductRespDto;
import hanghae.order_service.controller.resp.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "Cart Controller", description = "장바구니 서비스를 위한 API 컨트롤러입니다.")
public interface CartControllerDocs {

    @Operation(summary = "카트에 상품 추가", description = "사용자의 카트에 상품을 추가합니다. 상품 ID와 수량을 포함하여 요청합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품이 성공적으로 카트에 추가됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    ResponseEntity<?> addCartProduct(String userId, CartCreateReqDto reqDto);

    @Operation(summary = "카트에서 상품 삭제", description = "사용자의 카트에서 여러 상품을 삭제합니다. 삭제할 상품들의 ID를 요청 본문에 포함시킵니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품이 성공적으로 카트에서 삭제됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    ResponseEntity<?> deleteCartProduct(String userId, List<CartDeleteReqDto> reqDto);

    @Operation(summary = "카트 상품 수량 수정", description = "사용자의 카트에서 상품의 수량을 수정합니다. 수정할 상품의 ID와 새로운 수량을 요청 본문에 포함시킵니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품의 수량이 성공적으로 수정됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    ResponseEntity<?> updateCartProduct(String userId, CartUpdateReqDto reqDto);

    @Operation(summary = "카트 상품 목록 조회", description = "사용자의 카트에 담긴 모든 상품을 조회합니다. 사용자 ID를 제공하여 해당 카트에 담긴 상품들을 가져옵니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 목록 조회 성공", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ProductRespDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    ResponseEntity<?> getCartProduct(String userId);
}
