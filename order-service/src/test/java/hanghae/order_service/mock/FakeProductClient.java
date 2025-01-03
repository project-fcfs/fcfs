package hanghae.order_service.mock;

import hanghae.order_service.controller.resp.ResponseDto;
import hanghae.order_service.domain.product.Product;
import hanghae.order_service.service.port.ProductClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
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
    public ResponseDto<List<Product>> processOrder(Map<String, Integer> cartProducts) {
        List<Product> products = data.stream().map(i -> {
            Integer orderCount = cartProducts.get(i.productId());
            return i.convertCart(i.quantity() - orderCount);
        }).toList();

        for (Product product : products) {
            data.removeIf(i -> i.productId().equals(product.productId()));
        }
        products.forEach(this::addData);

        boolean hasNegativeStock = products.stream().anyMatch(i -> i.quantity() < 0);
        return hasNegativeStock ? new ResponseDto<>(-1, "fail", null, HttpStatus.BAD_REQUEST)
                : new ResponseDto<>(1, "success", data, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Product>> getProducts(List<String> productIds) {
        List<Product> products = data.stream().filter(i -> productIds.contains(i.productId()))
                .toList();
        return ResponseEntity.ok().body(products);
    }
}
