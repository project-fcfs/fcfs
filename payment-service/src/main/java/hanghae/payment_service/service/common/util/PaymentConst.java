package hanghae.payment_service.service.common.util;

public enum PaymentConst {

    SUCCESS(1,"결제 성공"),
    FAIL(-1, "결제 실패")
    ;

    private int code;
    private String description;

    PaymentConst(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
