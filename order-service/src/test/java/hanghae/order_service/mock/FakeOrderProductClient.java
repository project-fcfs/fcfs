package hanghae.order_service.mock;

import hanghae.order_service.domain.product.OrderItem;
import hanghae.order_service.infrastructure.product.ItemRefund;
import hanghae.order_service.service.port.OrderProductClient;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FakeOrderProductClient implements OrderProductClient {

    private List<OrderItem> orderItems;

    public FakeOrderProductClient(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @Override
    public List<OrderItem> getOrderItems(List<String> productIds) {
        return orderItems.stream().filter(i -> productIds.contains(i.productId())).toList();
    }

    @Override
    public void addProductStock(List<ItemRefund> itemRefunds) {
        Map<String, Integer> map = itemRefunds.stream()
                .collect(Collectors.toMap(k -> k.productId(), v -> v.count()));

        orderItems = orderItems.stream()
                .map(i -> {
                    Integer quantity = map.get(i.productId());
                    int i1 = i.orderCount() + quantity;
                    return new OrderItem(i.productId(), i.orderPrice(), i1);
                }).toList();
    }
}
