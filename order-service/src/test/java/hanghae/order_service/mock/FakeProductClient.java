package hanghae.order_service.mock;

import hanghae.order_service.controller.resp.ResponseDto;
import hanghae.order_service.service.port.ProductClient;
import org.springframework.http.ResponseEntity;

public class FakeProductClient implements ProductClient {
    @Override
    public ResponseEntity<ResponseDto<?>> isValidProduct(String productId) {
        return null;
    }
}
