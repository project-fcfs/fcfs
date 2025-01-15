package hanghae.order_service.service.common.exception;

public enum ErrorCode {
    // 공통 에러
    INVALID_REQUEST(1000, "올바르지 않은 요청입니다."),
    ERROR_PARSE_DATA(1001, "문자열 변환 중에 문제가 발생했습니다."),
    INVALID_DATA_BINDING(1002, "유효하지 않은 바인딩입니다."),
    INTERNAL_SERVER_ERROR(1003, "서버 에러가 발생하였습니다. 관리자에게 문의해 주세요."),

    // 장바구니 관련 에러
    NOT_FOUND_CART_PRODUCT(3001, "요청하신 장바구니 속 상품을 찾을 수 없습니다"),
    OUT_OF_STOCK_CART(3002, "장바구니 속 상품의 수량보다 많습니다."),

    // 주문 관련 에러
    NOT_FOUND_ORDER(3101, "요청하신 주문 정보가 없습니다"),
    ERROR_CANNOT_CANCEL_SHIPPING(3102, "이미 배송중인 상품은 취소할 수 없습니다."),
    ERROR_CANNOT_RETURN_SHIPPED(3103, "배송완료된 상품은 반품할 수 없습니다."),
    ERROR_REQUEST_REFUND(3104, "주문 완료된 상품만 환불 신청이 가능합니다."),
    ERROR_COMPLETE_REFUND(3105, "환불 신청한 상품만 환불이 가능합니다."),
    OUT_OF_STOCK(3106, "주문하신 상품의 재고가 부족합니다."),
    NOT_OPEN_TIME(3107, "오픈 시간 전입니다."),
    INVALID_PRODUCT(3108, "올바르지 않은 상품입니다.");

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

