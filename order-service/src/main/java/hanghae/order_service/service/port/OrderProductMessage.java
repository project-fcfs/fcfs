package hanghae.order_service.service.port;

import hanghae.order_service.domain.order.OrderProduct;
import java.util.List;

public interface OrderProductMessage {

    void removeStock(List<OrderProduct> orderProducts);

    void restoreStock(List<OrderProduct> orderProducts);
}
