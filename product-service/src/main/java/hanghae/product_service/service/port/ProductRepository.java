package hanghae.product_service.service.port;

import hanghae.product_service.domain.product.Product;
import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);

    Optional<Product> fetchByUid(String uid);
}
