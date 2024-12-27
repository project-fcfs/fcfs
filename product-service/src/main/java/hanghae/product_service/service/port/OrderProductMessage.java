package hanghae.product_service.service.port;

public interface OrderProductMessage {
    void sendResult(int code, String message);
}
