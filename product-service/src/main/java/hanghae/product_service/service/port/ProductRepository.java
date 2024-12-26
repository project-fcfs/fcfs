package hanghae.product_service.service.port;

import hanghae.product_service.domain.product.Product;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);

    Optional<Product> findProductById(String uid);

    List<Product> findAll();

    List<Product> findAllByProductIds(List<String> ids);
}
