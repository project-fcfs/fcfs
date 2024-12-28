package hanghae.product_service.service.port;

public interface OrderProductMessage<T> {
    void sendResult(int code, String message, T data);
}
