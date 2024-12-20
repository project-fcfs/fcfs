package hanghae.order_service.domain.order;

public enum DeliveryStatus {
    PREPARING("배달 준비중"),
    COMPLETED("배달 완료"),
    CANCELED("배달 취소");

    private final String description;

    DeliveryStatus(String description) {
        this.description = description;
    }

}
