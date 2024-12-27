package hanghae.order_service.infrastructure.product;

import hanghae.order_service.domain.order.OrderProduct;
import hanghae.order_service.service.port.OrderProductMessage;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class OrderProductMessageImpl implements OrderProductMessage {

    @Override
    public void updateStock(List<OrderProduct> orderProducts, String orderId) {

    }

    @Override
    public void restoreStock(List<OrderProduct> orderProducts) {

    }
}
