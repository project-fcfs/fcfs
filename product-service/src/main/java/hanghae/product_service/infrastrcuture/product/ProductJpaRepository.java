package hanghae.product_service.infrastrcuture.product;

import hanghae.product_service.domain.product.Product;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> findByUUID(String uid);
}
