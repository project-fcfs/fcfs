package hanghae.order_service.infrastructure.cart;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartProductJpaRepository extends JpaRepository<CartProductEntity, Long> {
    @Query("""
            select cp from CartProductEntity cp
            join cp.cartEntity c
            where c.userId = :userId
            and cp.productId in :productIds
            """)
    List<CartProductEntity> findAllCartProduct(@Param("userId") String userId, @Param("productIds") List<String> productIds);

    @Query("""
           select cp from CartProductEntity cp
           join cp.cartEntity c
           where c.userId = :userId
           and cp.productId = :productId
           """)
    Optional<CartProductEntity> findProductByProductAndUser(@Param("productId") String productId, @Param("userId") String userId);

    @Query("""
            select cp from CartProductEntity cp
            join cp.cartEntity c
            where c.userId = :userId
            """)
    List<CartProductEntity> findAllByUserId(@Param("userId") String userId);
}
