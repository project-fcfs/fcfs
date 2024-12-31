package hanghae.payment_service.service.port;

public interface OrderMessage {

    void sendOrderDecide(int code, String message, String orderId);
}
