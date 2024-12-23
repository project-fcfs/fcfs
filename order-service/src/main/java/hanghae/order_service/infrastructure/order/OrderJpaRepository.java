package hanghae.order_service.infrastructure.order;

import hanghae.order_service.domain.order.Order;
import hanghae.order_service.domain.order.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {
    @Query("select o from OrderEntity o where o.userId = :userId and o.orderId = :orderId")
    Optional<OrderEntity> findByUserOrderByOrderId(@Param("userId") String userId, @Param("orderId") String orderId);

    @Query("select 0 from OrderEntity o where o.orderStatus = :status and o.updatedAt <= :currentDate")
    List<OrderEntity> findOrdersStatusByDate(@Param("status") OrderStatus orderStatus, @Param("currentDate") LocalDateTime dateWithMinusDay);
}
