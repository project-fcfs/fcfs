package hanghae.product_service.infrastrcuture.product;

import hanghae.product_service.domain.product.ProductType;
import jakarta.persistence.LockModeType;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
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

    @Query("""
            select p from ProductEntity p
            where p.type = :type
            and p.id > :cursor
            """)
    List<ProductEntity> findAllByType(@Param("cursor") Long cursor, @Param("type") ProductType type, Pageable pageable);

    /*// 여러 키에 대해 락을 획득하는 쿼리
    @Query(value = "SELECT get_lock(:keys, 3000)", nativeQuery = true)
    void getLocks(List<String> keys);

    // 여러 키에 대해 락을 해제하는 쿼리
    @Query(value = "SELECT release_lock(:keys)", nativeQuery = true)
    void releaseLocks(List<String> keys);*/
}
