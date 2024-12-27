package hanghae.product_service.service.common.util;

public enum ErrorMessage {

    NOT_FOUND_PRODUCT("요청하신 상품을 찾을 수 없습니다."),
    OUT_OF_STOCK("요청하신 상품의 재고가 부족합니다"),
    ERROR_PARSE_JSON("문자열 변환 중에 문제가 발생했습니다.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return "[ERROR] " + message;
    }
}
