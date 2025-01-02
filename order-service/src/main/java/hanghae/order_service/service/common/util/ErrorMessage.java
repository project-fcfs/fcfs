package hanghae.order_service.service.common.util;

public enum ErrorMessage {

    INVALID_PRODUCT("유효하지 않은 상품입니다."),
    NOT_FOUND_CART_PRODUCT("요청하신 장바구니 속 상품을 찾을 수 없습니다"),
    NOT_FOUND_ORDER("요청하신 주문 정보가 없습니다."),
    OUT_OF_STOCK_CART("장바구니 속 상품의 수량보다 많습니다."),
    ERROR_CANNOT_CANCEL_SHIPPING("이미 배송중인 상품은 취소할 수 없습니다."),
    ERROR_CANNOT_RETURN_SHIPPED("배송완료된 상품은 반품할 수 없습니다."),
    ERROR_REQUEST_REFUND("주문 완료된 상품만 환불 신청이 가능합니다."),
    ERROR_COMPLETE_REFUND("환불 신청한 상품만 환불이 가능합니다."),
    ERROR_PARSE_JSON("문자열 변환 중에 문제가 발생했습니다."),
    OUT_OF_STOCK("주문하신 상품의 재고가 부족합니다."),
    NOT_OPEN_TIME("오픈 시간 전입니다.")
    ;

    private String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return "[ERROR] "+ message;
    }
}
