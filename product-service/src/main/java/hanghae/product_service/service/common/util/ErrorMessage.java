package hanghae.product_service.service.common.util;

public enum ErrorMessage {

    NOT_FOUND_PRODUCT("요청하신 상품을 찾을 수 없습니다."),
    OUT_OF_STOCK("요청하신 상품의 재고가 부족합니다")
    ;

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return "[ERROR] " + message;
    }
}
