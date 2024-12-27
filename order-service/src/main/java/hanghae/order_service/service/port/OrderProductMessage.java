package hanghae.order_service.service.port;

import hanghae.order_service.domain.order.OrderProduct;
import java.util.List;

public interface OrderProductMessage {

    void updateStock(List<OrderProduct> orderProducts);
}
