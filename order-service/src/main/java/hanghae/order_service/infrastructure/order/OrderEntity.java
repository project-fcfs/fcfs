package hanghae.order_service.infrastructure.order;

import hanghae.order_service.domain.order.Order;
import hanghae.order_service.domain.order.OrderProduct;
import hanghae.order_service.domain.order.OrderStatus;
import hanghae.order_service.infrastructure.delivery.DeliveryEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false, unique = true)
    private String orderId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderProductEntity> orderProducts = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private DeliveryEntity delivery;

    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    protected OrderEntity() {
    }

    public OrderEntity(Long id, OrderStatus orderStatus, String userId, String orderId,
                       List<OrderProductEntity> orderProducts, DeliveryEntity delivery, LocalDateTime createdAt,
                       LocalDateTime updatedAt) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.userId = userId;
        this.orderId = orderId;
        this.orderProducts = orderProducts;
        this.delivery = delivery;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * 생성 메서드
     */
    public static OrderEntity fromModel(Order order) {
        OrderEntity orderEntity = new OrderEntity();

        orderEntity.id = order.id();
        orderEntity.orderStatus = order.orderStatus();
        orderEntity.userId = order.userId();
        orderEntity.orderId = order.orderId();
        orderEntity.createdAt = order.createdAt();
        orderEntity.updatedAt = order.updatedAt();

        for (OrderProduct orderProduct : order.orderProducts()) {
            OrderProductEntity orderProductEntity = OrderProductEntity.fromModel(orderProduct);
            orderEntity.addOrderItem(orderProductEntity);
        }

        orderEntity.delivery = DeliveryEntity.fromModel(order.delivery());

        return orderEntity;
    }

    public Order toModel() {
        List<OrderProduct> orderProduct = orderProducts.stream().map(OrderProductEntity::toModel).toList();
        return new Order(id, orderStatus, userId, orderId, orderProduct, delivery.toModel(), createdAt, updatedAt);
    }

    /**
     * 연관관계 메서드
     */
    public void addOrderItem(OrderProductEntity orderProduct) {
        orderProducts.add(orderProduct);
        orderProduct.setOrderEntity(this);
    }
}
