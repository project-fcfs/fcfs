package hanghae.product_service.infrastrcuture.product;

import hanghae.product_service.domain.product.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> findByProductId(String productId);

    @Query("select p from ProductEntity p where p.id in :ids")
    List<ProductEntity> findAllByProductIds(@Param("ids") List<String> ids);
}
