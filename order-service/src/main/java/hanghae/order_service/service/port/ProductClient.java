package hanghae.order_service.service.port;

import hanghae.order_service.controller.resp.ResponseDto;
import hanghae.order_service.domain.product.Product;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;

public interface ProductClient {

    ResponseEntity<?> isValidProduct(String productId);

    ResponseDto<List<Product>> processOrder(Map<String, Integer> cartProducts);

    ResponseEntity<List<Product>> getProducts(List<String> productIds);
}
