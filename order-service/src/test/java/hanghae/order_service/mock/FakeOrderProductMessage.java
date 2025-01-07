package hanghae.order_service.mock;

import hanghae.order_service.domain.order.OrderProduct;
import hanghae.order_service.service.port.OrderProductMessage;
import java.util.ArrayList;
import java.util.List;

public class FakeOrderProductMessage implements OrderProductMessage {

    public List<FakeProduct> data = new ArrayList<>();

    public void addProduct(FakeProduct product) {
        data.add(product);
    }

    @Override
    public void removeStock(List<OrderProduct> orderProducts) {
        data.forEach(product -> {
            orderProducts.forEach(orderProduct -> {
                if (product.getProductId().equals(orderProduct.productId())) {
                    product.removeStock(orderProduct.orderCount());
                }
            });
        });
    }

    @Override
    public void restoreStock(List<OrderProduct> orderProducts) {
        data.forEach(product -> {
            orderProducts.forEach(orderProduct -> {
                if (product.getProductId().equals(orderProduct.productId())) {
                    product.addStock(orderProduct.orderCount());
                }
            });
        });
    }

    public FakeProduct getProductById(Long productId) {
        return data.stream().filter(product -> product.getProductId().equals(productId)).findFirst().get();
    }


}
