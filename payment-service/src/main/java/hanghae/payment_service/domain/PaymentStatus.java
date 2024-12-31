package hanghae.payment_service.domain;

public enum PaymentStatus {
    SUCCESS("결제 성공"), FAIL("결제 실패");
    String description;

    PaymentStatus(String description) {
        this.description = description;
    }
}
