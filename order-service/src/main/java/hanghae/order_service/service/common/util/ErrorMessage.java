package hanghae.order_service.service.common.util;

public enum ErrorMessage {

    NOT_FOUND_CART_PRODUCT("요청하신 장바구니 속 상품을 찾을 수 없습니다"),
    OUT_OF_STOCK_CART("장바구니 속 상품의 수량보다 많습니다.")
    ;

    private String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return "[ERROR] "+ message;
    }
}
