package hanghae.order_service.service.port;

import hanghae.order_service.domain.order.OrderProduct;
import java.util.List;

public interface OrderProducerMessage {

    void removeStock(List<OrderProduct> orderProducts, String orderId);

    void restoreStock(List<OrderProduct> orderProducts);
}
