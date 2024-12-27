package hanghae.order_service.service.port;

import hanghae.order_service.domain.product.Product;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface ProductClient {

    ResponseEntity<?> isValidProduct(String productId);

    ResponseEntity<List<Product>> getProducts(List<String> productIds);
}
