package hanghae.product_service.service.common.exception;

public enum ErrorCode {

    // 공통 에러
    INVALID_REQUEST(1000, "올바르지 않은 요청입니다."),
    ERROR_PARSE_DATA(1001, "문자열 변환 중에 문제가 발생했습니다."),
    INVALID_DATA_BINDING(1002, "유효하지 않은 바인딩입니다."),
    INTERNAL_SERVER_ERROR(1003, "서버 에러가 발생하였습니다. 관리자에게 문의해 주세요."),

    // 상품 에러
    NOT_FOUND_PRODUCT(2001, "요청하신 상품을 찾을 수 없습니다."),
    OUT_OF_STOCK(2002,"요청하신 상품의 재고가 부족합니다.");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
