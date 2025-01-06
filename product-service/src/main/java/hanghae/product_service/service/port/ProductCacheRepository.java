package hanghae.product_service.service.port;

import hanghae.product_service.domain.product.Product;
import java.util.List;

public interface ProductCacheRepository {
    void save(Product product);
    Boolean removeStock(List<String> productIds, List<String> orderCounts);
    Boolean restoreStock(List<String> productIds, List<String> orderCounts);
}
