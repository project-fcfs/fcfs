package hanghae.product_service.service.port;

import hanghae.product_service.domain.product.Product;
import hanghae.product_service.domain.product.ProductType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface ProductRepository {
    Product save(Product product);

    Optional<Product> findProductById(Long id);

    List<Product> findAllByType(Long cursor, ProductType type, Pageable pageable);

    List<Product> findAllByProductIds(List<Long> ids);

    List<Product> saveAll(List<Product> products);
}
