package hanghae.product_service.infrastrcuture.product;

import hanghae.product_service.domain.product.Product;
import hanghae.product_service.domain.product.ProductType;
import hanghae.product_service.service.port.ProductRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
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
    public Optional<Product> findProductById(Long productId) {
        return jpaRepository.findById(productId).map(ProductEntity::toModel);
    }

    @Override
    public List<Product> findAllByType(Long cursor,ProductType type, Pageable pageable) {
        return jpaRepository.findAllByType(cursor,type, pageable).stream().map(ProductEntity::toModel).toList();
    }

    @Override
    public List<Product> findAllByProductIds(List<Long> ids) {
        return jpaRepository.findAllByProductIds(ids).stream().map(ProductEntity::toModel).toList();
    }

    @Override
    public List<Product> saveAll(List<Product> products) {
        List<ProductEntity> entities = products.stream().map(ProductEntity::fromModel).toList();
        List<ProductEntity> savedEntities = jpaRepository.saveAll(entities);
        return savedEntities.stream().map(ProductEntity::toModel).toList();
    }
}
