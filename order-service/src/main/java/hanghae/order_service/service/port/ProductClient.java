package hanghae.order_service.service.port;

import hanghae.order_service.controller.resp.ResponseDto;
import hanghae.order_service.domain.product.Product;
import java.util.List;
import java.util.Map;

public interface ProductClient {

    ResponseDto<?> isValidProduct(String productId);

    ResponseDto<List<Product>> processOrder(Map<String, Integer> cartProducts);

    ResponseDto<List<Product>> getProducts(List<String> productIds);
}
