package hanghae.order_service.mock;

import hanghae.order_service.domain.product.Product;
import hanghae.order_service.service.port.ProductClient;
import java.util.List;
import org.springframework.http.ResponseEntity;

public class FakeProductClient implements ProductClient {

    @Override
    public ResponseEntity<?> isValidProduct(String productId) {
        return null;
    }

    @Override
    public ResponseEntity<List<Product>> getProducts(List<String> productIds) {
        return null;
    }
}
