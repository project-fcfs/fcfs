package hanghae.order_service.infrastructure.cart;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartJpaRepository extends JpaRepository<CartEntity, Long> {
    @Query("select c from CartEntity c where c.userId = :userId")
    Optional<CartEntity> findByUserId(@Param("userId") String userId);
}
