package hanghae.order_service.domain.order;

public enum OrderStatus {

    PENDING("대기상태"),
    COMPLETED("주문완료"),
    CANCELLED("주문취소"),
    RETURN_REQUESTED("환불신청"),
    RETURN_COMPLETED("환불완료");

    private String description;

    OrderStatus(String description) {
        this.description = description;
    }

}
