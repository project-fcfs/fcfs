package hanghae.payment_service.infrastructure.payment;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, Long> {
    @Query("select p from PaymentEntity p where p.orderId = :orderId")
    Optional<PaymentEntity> findByOrderId(@Param("orderId") String orderId);
}
