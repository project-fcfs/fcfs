package hanghae.order_service.infrastructure.orderproduct;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductJpaRepository extends JpaRepository<OrderProductEntity, Long> {
}
