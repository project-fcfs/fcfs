package hanghae.payment_service.mock;

import hanghae.payment_service.service.port.OrderMessage;

public class FakeOrderMessage implements OrderMessage {
    private int code;
    private String message;
    private String orderId;

    public int getCode() {
        return code;
    }

    public String getOrderId() {
        return orderId;
    }

    @Override
    public void sendOrderDecide(int code, String message, String orderId) {
        this.code = code;
        this.message = message;
        this.orderId = orderId;
    }
}
