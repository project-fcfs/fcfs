package hanghae.order_service.service.common.util;

public enum ErrorMessage {

    NOT_FOUND_CART_PRODUCT("요청하신 장바구니 속 상품을 찾을 수 없습니다"),
    NOT_FOUND_ORDER("요청하신 주문 정보가 없습니다."),
    OUT_OF_STOCK_CART("장바구니 속 상품의 수량보다 많습니다."),
    ERROR_CANNOT_CANCEL_SHIPPING("이미 배송중인 상품은 취소할 수 없습니다."),
    ERROR_CANNOT_RETURN_SHIPPED("배송완료된 상품은 반품할 수 없습니다."),
    ERROR_REQUEST_REFUND("주문 완료된 상품만 환불 신청이 가능합니다."),
    ERROR_COMPLETE_REFUND("환불 신청한 상품만 환불이 가능합니다.")
    ;

    private String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return "[ERROR] "+ message;
    }
}
