package hanghae.order_service.mock;

import hanghae.order_service.domain.order.OrderProduct;
import hanghae.order_service.service.port.OrderProductMessage;
import java.util.List;

public class FakeOrderProductMessage implements OrderProductMessage {

    @Override
    public void updateStock(List<OrderProduct> orderProducts, String orderId) {

    }
}
