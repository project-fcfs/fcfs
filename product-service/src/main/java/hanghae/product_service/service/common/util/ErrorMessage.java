package hanghae.product_service.service.common.util;

public enum ErrorMessage {

    NOT_FOUND_PRODUCT("요청하신 상품을 찾을 수 없습니다.")
    ;

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return "[ERROR] " + message;
    }
}
