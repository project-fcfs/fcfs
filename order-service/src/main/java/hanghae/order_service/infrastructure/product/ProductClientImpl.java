package hanghae.order_service.infrastructure.product;

import hanghae.order_service.domain.product.OrderItem;
import hanghae.order_service.service.port.ProductClient;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ProductClientImpl implements ProductClient {
    @Override
    public List<OrderItem> getOrderItems(List<String> productIds) {
        return List.of();
    }
}
