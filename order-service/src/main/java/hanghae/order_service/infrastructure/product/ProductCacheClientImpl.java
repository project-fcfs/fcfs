package hanghae.order_service.infrastructure.product;

import hanghae.order_service.controller.resp.ResponseDto;
import hanghae.order_service.domain.product.Product;
import hanghae.order_service.service.port.ProductClient;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

//@Component
public class ProductCacheClientImpl implements ProductClient {
    @Override
    public ResponseDto<?> isValidProduct(String productId) {
        return null;
    }

    @Override
    public ResponseDto<List<Product>> processOrder(Map<String, Integer> cartProducts) {
        return null;
    }

    @Override
    public ResponseDto<List<Product>> getProducts(List<String> productIds) {
        return null;
    }
}
