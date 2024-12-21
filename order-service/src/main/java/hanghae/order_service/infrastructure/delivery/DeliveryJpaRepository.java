package hanghae.order_service.infrastructure.delivery;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryJpaRepository extends JpaRepository<DeliveryEntity, Long> {
}
