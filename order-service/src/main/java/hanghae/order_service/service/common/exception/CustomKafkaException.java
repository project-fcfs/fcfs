package hanghae.order_service.service.common.exception;

public class CustomKafkaException extends RuntimeException {
    public CustomKafkaException(String message, Throwable cause) {
        super(message, cause);
    }
}
