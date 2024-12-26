package hanghae.product_service.infrastrcuture.product;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> findByProductId(String productId);

    @Query("select p from ProductEntity p where p.productId in :productIds")
    List<ProductEntity> findAllByProductIds(@Param("productIds") List<String> productIds);
}
