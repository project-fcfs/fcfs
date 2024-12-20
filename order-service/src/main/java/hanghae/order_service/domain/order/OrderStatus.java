package hanghae.order_service.domain.order;

public enum OrderStatus {

    COMPLETED("주문완료"),
    CANCELLED("주문취소"),
    RETURN_REQUESTED("반품신청"),
    RETURN_COMPLETED("반품완료");
    private String description;

    OrderStatus(String description) {
        this.description = description;
    }

}
