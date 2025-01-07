package hanghae.order_service.service.port;

import hanghae.order_service.controller.resp.ResponseDto;
import hanghae.order_service.domain.product.Product;
import java.util.List;
import java.util.Map;

public interface ProductClient {

    Boolean isValidProduct(Long productId);

    ResponseDto<List<Product>> processOrder(Map<Long, Integer> cartProducts);

    List<Product> getProducts(List<Long> productIds);
}
