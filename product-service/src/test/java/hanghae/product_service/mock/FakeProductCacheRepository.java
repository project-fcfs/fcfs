package hanghae.product_service.mock;

import hanghae.product_service.domain.product.Product;
import hanghae.product_service.service.port.ProductCacheRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class FakeProductCacheRepository implements ProductCacheRepository {
    private List<Product> data = new ArrayList<>();
    private AtomicLong counter = new AtomicLong();

    @Override
    public void save(Product product) {
        if (product.id() == null || product.id() == 0L) {
            Product newProduct = new Product(counter.incrementAndGet(),
                    product.name(), product.price(), product.quantity(),
                    product.type(), product.status());
            data.add(newProduct);
        } else {
            data.removeIf(i -> i.id().equals(product.id()));
            data.add(product);
        }
    }

    @Override
    public Boolean removeStock(List<String> productIds, List<String> orderCounts) {
        return null;
    }

    @Override
    public Boolean restoreStock(List<String> productIds, List<String> orderCounts) {
        return null;
    }
}
