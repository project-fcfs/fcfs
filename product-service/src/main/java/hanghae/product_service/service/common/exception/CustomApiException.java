package hanghae.product_service.service.common.exception;

public class CustomApiException extends RuntimeException {
    public CustomApiException(String message) {
        super(message);
    }

    public CustomApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
