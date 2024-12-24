package hanghae.order_service.mock;

import hanghae.order_service.domain.order.Order;
import hanghae.order_service.domain.order.OrderStatus;
import hanghae.order_service.service.port.OrderRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class FakeOrderRepository implements OrderRepository {
    private List<Order> data = new ArrayList<>();
    private AtomicLong counter = new AtomicLong();

    @Override
    public Order save(Order input) {
        if (input.id() == null || input.id() == 0L) {
            Order newDelivery = new Order(counter.incrementAndGet(),
                    input.orderStatus(), input.userId(), input.orderId(), input.orderProducts(),
                    input.delivery(), input.createdAt(), input.updatedAt());
            data.add(newDelivery);
            return newDelivery;
        } else {
            data.removeIf(i -> i.id().equals(input.id()));
            data.add(input);
            return input;
        }
    }

    @Override
    public Optional<Order> findByUserOrderByOrderId(String userId, String orderId) {
        return data.stream().filter(i -> i.userId().equals(userId) && i.orderId().equals(orderId)).findFirst();
    }

    @Override
    public List<Order> findAllRequestRefund(OrderStatus orderStatus, LocalDateTime dateWithMinusDay) {
        return data.stream().filter(i -> i.orderStatus().equals(orderStatus))
                .filter(i -> !i.updatedAt().isBefore(dateWithMinusDay))
                .toList();
    }

    @Override
    public void saveAll(List<Order> orders) {
        orders.forEach(this::save);
    }

    public Optional<Order> findById(Long id){
        return data.stream().filter(i -> i.id().equals(id)).findFirst();
    }
}
