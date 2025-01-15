package hanghae.product_service.controller;

import hanghae.product_service.controller.req.ProductCreateReqDto;
import hanghae.product_service.controller.req.StockUpdateReqDto;
import hanghae.product_service.controller.resp.ProductRespDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Product Controller", description = "상품 서비스를 위한 API 컨트롤러입니다.")
public interface ProductControllerDocs {

    @Parameters(value = {
            @Parameter(name = "createReqDto", description = "상품 생성에 필요한 정보 (상품 이름, 가격, 수량, 타입)", required = true),
            @Parameter(name = "file", description = "상품 이미지 파일 (선택사항)", required = false)
    })
    @Operation(summary = "상품 생성", description = "새로운 상품을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "상품이 성공적으로 생성됨",
                    content = @Content(schema = @Schema(implementation = ProductRespDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    ResponseEntity<?> createProduct(ProductCreateReqDto reqDto, MultipartFile file);

    @Parameter(name = "id", description = "조회할 상품의 ID", required = true)
    @Operation(summary = "상품 조회", description = "ID를 이용해 특정 상품을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공적으로 상품 정보를 반환합니다.",
                    content = @Content(schema = @Schema(implementation = ProductRespDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    ResponseEntity<?> getProduct(Long productId);

    @Parameters(value = {
            @Parameter(name = "cursor", description = "조회 시작 위치(기본값: 0)", required = false),
            @Parameter(name = "size", description = "조회할 상품의 수 (기본값: 10)", required = false)
    })
    @Operation(summary = "상품 목록 조회", description = "상품 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 목록 반환"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    ResponseEntity<?> getProducts(Long cursor, int size);

    @Parameters(value = {
            @Parameter(name = "cursor", description = "조회 시작 위치(기본값: 0)", required = false),
            @Parameter(name = "size", description = "조회할 상품의 수 (기본값: 10)", required = false)
    })
    @Operation(summary = "선착순 상품 조회", description = "선착순으로 상품 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "선착순 상품 목록 반환"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    ResponseEntity<?> getFcfsProducts(Long cursor, int size);

    @Parameter(name = "ids", description = "조회할 상품들의 ID 리스트", required = true)
    @Operation(summary = "상품 ID로 상품 조회", description = "주어진 상품 ID 목록을 기반으로 해당 상품들을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청: 유효하지 않은 상품 ID"),
    })
    ResponseEntity<?> fetchProductsById(List<Long> ids);

    @Parameter(name = "dtos", description = "주문할 상품과 수량 정보", required = true)
    @Operation(summary = "주문 처리", description = "주문을 처리하고 상품의 수량을 업데이트합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "주문 처리 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    ResponseEntity<?> processOrder(List<StockUpdateReqDto> dtos);
}
