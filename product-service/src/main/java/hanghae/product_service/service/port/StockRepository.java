package hanghae.product_service.service.port;

import hanghae.product_service.domain.product.Product;
import java.util.List;

public interface StockRepository {

    List<Product> findAllByProductIdsWithPessimistic(List<String> ids);
    List<Product> saveAll(List<Product> products);
}
