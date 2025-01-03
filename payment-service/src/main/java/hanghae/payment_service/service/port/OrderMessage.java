package hanghae.payment_service.service.port;

import org.springframework.http.HttpStatus;

public interface OrderMessage {

    void sendOrderDecide(int code, String message, String orderId, HttpStatus httpStatus);
}
