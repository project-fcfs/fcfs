package hanghae.order_service.service.port;

import hanghae.order_service.domain.product.OrderItem;
import hanghae.order_service.infrastructure.product.ItemRefund;
import java.util.List;

public interface ProductClient {

    List<OrderItem> getOrderItems(List<String> productIds);
    void addProductStock(List<ItemRefund> itemRefunds);
}
