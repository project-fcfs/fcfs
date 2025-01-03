package hanghae.product_service.infrastrcuture.product;

import hanghae.product_service.domain.product.Product;
import hanghae.product_service.service.port.ProductRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepositoryImpl implements ProductRepository {
    private final ProductJpaRepository jpaRepository;

    public ProductRepositoryImpl(ProductJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Product save(Product product) {
        return jpaRepository.save(ProductEntity.fromModel(product)).toModel();
    }

    @Override
    public Optional<Product> findProductById(String productId) {
        return jpaRepository.findByProductId(productId).map(ProductEntity::toModel);
    }

    @Override
    public List<Product> findAll() {
        return jpaRepository.findAll().stream().map(ProductEntity::toModel).toList();
    }

    @Override
    public List<Product> findAllByProductIds(List<String> ids) {
        return jpaRepository.findAllByProductIds(ids).stream().map(ProductEntity::toModel).toList();
    }

    @Override
    public List<Product> saveAll(List<Product> products) {
        List<ProductEntity> entities = products.stream().map(ProductEntity::fromModel).toList();
        List<ProductEntity> savedEntities = jpaRepository.saveAll(entities);
        return savedEntities.stream().map(ProductEntity::toModel).toList();
    }
}
