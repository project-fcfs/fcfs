package hanghae.order_service.mock;

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
                .filter(i -> i.productId().equals(productId))  // 필터링
                .findFirst()  // 첫 번째 항목 찾기
                .map(product -> ResponseEntity.ok().build())  // 찾은 경우 200 OK 응답 반환
                .orElseGet(() -> ResponseEntity.notFound().build());  // 못 찾은 경우 404 Not Found 응답 반환
    }

    @Override
    public ResponseEntity<List<Product>> getProducts(List<String> productIds) {
        List<Product> products = data.stream().filter(i -> productIds.contains(i.productId()))
                .toList();
        return ResponseEntity.ok().body(products);
    }
}
