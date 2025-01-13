package hanghae.product_service.infrastrcuture.product;

import hanghae.product_service.domain.product.Product;
import hanghae.product_service.service.port.StockRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class StockRepositoryImpl implements StockRepository {

    private final ProductJpaRepository jpaRepository;

    public StockRepositoryImpl(ProductJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Product> findAllByProductIdsWithPessimistic(List<Long> ids) {
        return jpaRepository.findAllByProductIdsWithPessimistic(ids)
                .stream().map(ProductEntity::toModel).toList();
    }

    @Override
    public List<Product> saveAll(List<Product> products) {
        List<ProductEntity> entities = products.stream().map(ProductEntity::fromModel).toList();
        return jpaRepository.saveAll(entities).stream().map(ProductEntity::toModel).toList();
    }
}
