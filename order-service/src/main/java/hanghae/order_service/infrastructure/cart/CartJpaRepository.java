package hanghae.order_service.infrastructure.cart;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CartJpaRepository extends JpaRepository<CartEntity, Long> {
}
