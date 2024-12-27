package hanghae.order_service.mock;

import hanghae.order_service.controller.resp.ProductRespDto;
import hanghae.order_service.service.port.ProductClient;
import java.util.List;
import org.springframework.http.ResponseEntity;

public class FakeProductClient implements ProductClient {

    @Override
    public ResponseEntity<?> isValidProduct(String productId) {
        return null;
    }

    @Override
    public ResponseEntity<List<ProductRespDto>> getProducts(List<String> productIds) {
        return null;
    }
}
