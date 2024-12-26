package hanghae.order_service.infrastructure.product;

import hanghae.order_service.domain.product.OrderItem;
import hanghae.order_service.service.port.OrderProductClient;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class OrderProductClientImpl implements OrderProductClient {

    @Override
    public List<OrderItem> getOrderItems(List<String> productIds) {
        return List.of();
    }

    @Override
    public void addProductStock(List<ItemRefund> itemRefunds) {

    }
}
