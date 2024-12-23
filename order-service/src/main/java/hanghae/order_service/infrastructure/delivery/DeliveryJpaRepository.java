package hanghae.order_service.infrastructure.delivery;

import hanghae.order_service.domain.order.DeliveryStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DeliveryJpaRepository extends JpaRepository<DeliveryEntity, Long> {
    @Query("select d from DeliveryEntity d where d.updatedAt <= :currentDate and d.status = :status")
    List<DeliveryEntity> findAllByStatusAndDate(@Param("status") DeliveryStatus deliveryStatus, @Param("currentDate") LocalDateTime dateWithMinusDay);
}
