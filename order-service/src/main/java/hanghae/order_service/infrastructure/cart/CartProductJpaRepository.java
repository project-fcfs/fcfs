package hanghae.order_service.infrastructure.cart;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartProductJpaRepository extends JpaRepository<CartProductEntity, Long> {
    @Query("""
            select cp from CartProductEntity cp
            join cp.cartEntity c
            where c.userId = :userId
            and cp.productId in :productIds
            """)
    List<CartProductEntity> findAllCartProduct(@Param("userId") String userId,
                                               @Param("productIds") List<Long> productIds);

    @Query("""
            select cp from CartProductEntity cp
            join cp.cartEntity c
            where c.userId = :userId
            and cp.productId = :productId
            """)
    Optional<CartProductEntity> findProductByProductAndUser(@Param("productId") Long productId,
                                                            @Param("userId") String userId);

    @Query("""
            select cp from CartProductEntity cp
            join cp.cartEntity c
            where c.userId = :userId
            """)
    List<CartProductEntity> findAllByUserId(@Param("userId") String userId);

    @Modifying(clearAutomatically = true)
    @Query("""
            delete from CartProductEntity cp
            where cp.cartEntity.userId = :userId
            and cp.productId in :productIds
            """)
    void deleteAllOrderProducts(@Param("productIds") List<Long> productIds, @Param("userId") String userId);
}
