package hanghae.product_service.service.port;

import hanghae.product_service.domain.product.Product;

public interface ProductRepository {
    Product save(Product product);
}
