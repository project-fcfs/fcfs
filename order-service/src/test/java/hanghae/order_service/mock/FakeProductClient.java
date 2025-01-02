package hanghae.order_service.mock;

import hanghae.order_service.domain.order.OrderProduct;
import hanghae.order_service.domain.product.Product;
import hanghae.order_service.service.port.ProductClient;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.ResponseEntity;

public class FakeProductClient implements ProductClient {

    List<Product> data = new ArrayList<>();

    public void addData(Product value) {
        data.add(value);
    }

    @Override
    public ResponseEntity<?> isValidProduct(String productId) {
        return data.stream()
                .filter(i -> i.productId().equals(productId))
                .findFirst()
                .map(product -> ResponseEntity.ok().build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<List<Product>> getProducts(List<String> productIds) {
        List<Product> products = data.stream().filter(i -> productIds.contains(i.productId()))
                .toList();
        return ResponseEntity.ok().body(products);
    }

    @Override
    public ResponseEntity<?> removeStock(List<OrderProduct> orderProducts) {
        List<Integer> updatedQuantities = orderProducts.stream()
                .map(orderProduct -> data.stream()
                        .filter(i -> i.productId().equals(orderProduct.productId()))
                        .map(i -> i.quantity() - orderProduct.orderCount())
                        .findFirst()
                        .orElse(0))
                .toList();

        boolean hasNegativeStock = updatedQuantities.stream().anyMatch(quantity -> quantity < 0);
        return hasNegativeStock ? ResponseEntity.badRequest().build() : ResponseEntity.ok().build();
    }
}
