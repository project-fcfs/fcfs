package hanghae.order_service.infrastructure.orderproduct;

import hanghae.order_service.domain.order.OrderProduct;
import hanghae.order_service.service.port.OrderProductRepository;
import org.springframework.stereotype.Repository;

@Repository
public class OrderProductRepositoryImpl implements OrderProductRepository {
    private final OrderProductJpaRepository jpaRepository;

    public OrderProductRepositoryImpl(OrderProductJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(OrderProduct orderProduct) {

    }
}
