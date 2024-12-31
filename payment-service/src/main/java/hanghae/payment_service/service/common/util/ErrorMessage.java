package hanghae.payment_service.service.common.util;

public enum ErrorMessage {
    ERROR_PARSE_JSON("문자열 변환 중에 문제가 발생했습니다.");

    private String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return "[ERROR] " + message;
    }
}
