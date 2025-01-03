package hanghae.product_service.mock;

import hanghae.product_service.domain.product.Product;
import hanghae.product_service.service.port.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class FakeProductRepository implements ProductRepository {
    private List<Product> data = new ArrayList<>();
    private AtomicLong counter = new AtomicLong();

    @Override
    public Product save(Product product) {
        if (product.id() == null || product.id() == 0L) {
            Product newProduct = new Product(counter.incrementAndGet(),
                    product.name(), product.price(), product.quantity(),
                    product.productId(), product.productStatus());
            data.add(newProduct);
            return newProduct;
        } else {
            data.removeIf(i -> i.id().equals(product.id()));
            data.add(product);
            return product;
        }
    }

    @Override
    public Optional<Product> findProductById(String uid) {
        return data.stream().filter(i -> i.productId().equals(uid)).findFirst();
    }

    @Override
    public List<Product> findAll() {
        return data;
    }

    @Override
    public List<Product> findAllByProductIds(List<String> ids) {
        return data.stream().filter(i -> ids.contains(i.productId())).toList();
    }

    @Override
    public List<Product> saveAll(List<Product> products) {
        return products.stream().map(this::save).toList();
    }
}
