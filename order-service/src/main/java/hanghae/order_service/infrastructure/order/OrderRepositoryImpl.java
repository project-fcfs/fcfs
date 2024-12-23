package hanghae.order_service.infrastructure.order;

import hanghae.order_service.domain.order.Order;
import hanghae.order_service.domain.order.OrderStatus;
import hanghae.order_service.service.port.OrderRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryImpl implements OrderRepository {
    private final OrderJpaRepository jpaRepository;

    public OrderRepositoryImpl(OrderJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Order save(Order order) {
        return jpaRepository.save(OrderEntity.fromModel(order)).toModel();
    }

    @Override
    public Optional<Order> findByUserOrderByOrderId(String userId, String orderId) {
        return jpaRepository.findByUserOrderByOrderId(userId, orderId).map(OrderEntity::toModel);
    }

    @Override
    public List<Order> findAllRequestRefund(OrderStatus orderStatus, LocalDateTime dateWithMinusDay) {
        return jpaRepository.findOrdersStatusByDate(orderStatus, dateWithMinusDay).stream()
                .map(OrderEntity::toModel).toList();
    }

    @Override
    public void saveAll(List<Order> orders) {
        List<OrderEntity> entities = orders.stream().map(OrderEntity::fromModel).toList();
        jpaRepository.saveAll(entities);
    }
}
