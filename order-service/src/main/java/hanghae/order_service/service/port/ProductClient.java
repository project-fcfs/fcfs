package hanghae.order_service.service.port;

import hanghae.order_service.domain.product.OrderItem;
import java.util.List;

public interface ProductClient {

    List<OrderItem> getOrderItems(List<String> productIds);
}
