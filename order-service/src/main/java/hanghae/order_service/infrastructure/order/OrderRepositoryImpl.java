package hanghae.order_service.infrastructure.order;

import hanghae.order_service.domain.order.Order;
import hanghae.order_service.service.port.OrderRepository;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryImpl implements OrderRepository {
    private final OrderJpaRepository jpaRepository;

    public OrderRepositoryImpl(OrderJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Order save(Order order) {
        return null;
    }

    @Override
    public Order findByUuid(String orderUid) {
        return null;
    }
}
