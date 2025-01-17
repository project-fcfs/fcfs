package hanghae.order_service.mock;

import hanghae.order_service.controller.resp.ResponseDto;
import hanghae.order_service.domain.product.Product;
import hanghae.order_service.service.port.ProductClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;

public class FakeProductClient implements ProductClient {

    List<Product> data = new ArrayList<>();

    public void addData(Product value) {
        data.add(value);
    }

    @Override
    public Boolean isValidProduct(Long productId) {
        return data.stream()
                .anyMatch(i -> i.productId().equals(productId));
    }

    @Override
    public List<Product> processOrder(Map<Long, Integer> cartProducts) {
        List<Product> products = data.stream().map(i -> {
            Integer orderCount = cartProducts.get(i.productId());
            return i.convertCart(i.quantity() - orderCount);
        }).toList();

        for (Product product : products) {
            data.removeIf(i -> i.productId().equals(product.productId()));
        }
        products.forEach(this::addData);

        boolean hasNegativeStock = products.stream().anyMatch(i -> i.quantity() < 0);
        return hasNegativeStock ? null
                : data;
    }

    @Override
    public List<Product> getProducts(List<Long> productIds) {
        return data.stream().filter(i -> productIds.contains(i.productId()))
                .toList();
    }
}
