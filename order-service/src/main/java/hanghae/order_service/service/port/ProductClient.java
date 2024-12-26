package hanghae.order_service.service.port;

import hanghae.order_service.controller.resp.ProductRespDto;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface ProductClient {

    ResponseEntity<?> isValidProduct(String productId);

    ResponseEntity<List<ProductRespDto>> getProducts(List<String> productIds);
}
