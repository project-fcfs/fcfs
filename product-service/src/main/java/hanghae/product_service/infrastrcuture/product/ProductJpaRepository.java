package hanghae.product_service.infrastrcuture.product;

import jakarta.persistence.LockModeType;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {
    @Query("select p from ProductEntity p where p.id in :productIds")
    List<ProductEntity> findAllByProductIds(@Param("productIds") List<Long> productIds);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from ProductEntity p where p.id in :productIds")
    List<ProductEntity> findAllByProductIdsWithPessimistic(@Param("productIds") List<Long> ids);
}
