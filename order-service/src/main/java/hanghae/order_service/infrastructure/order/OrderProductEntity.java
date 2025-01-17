package hanghae.order_service.infrastructure.order;

import hanghae.order_service.domain.order.OrderProduct;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_product")
public class OrderProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private int orderPrice;
    @Column(nullable = false)
    private int orderCount;
    @Column(nullable = false)
    private Long productId;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    protected OrderProductEntity() {
    }

    public OrderProductEntity(Long id, int orderPrice, int orderCount, Long productId,
                              LocalDateTime createdAt, OrderEntity order) {
        this.id = id;
        this.orderPrice = orderPrice;
        this.orderCount = orderCount;
        this.productId = productId;
        this.createdAt = createdAt;
        this.order = order;
    }

    /**
     * 생성 메서드
     */
    public static OrderProductEntity fromModel(OrderProduct orderProduct){
        return new OrderProductEntity(orderProduct.id(), orderProduct.orderPrice(),
                orderProduct.orderCount(), orderProduct.productId(), orderProduct.createdAt(),
                orderProduct.order() == null ? null : OrderEntity.fromModel(orderProduct.order()));
    }

    public OrderProduct toModel(){
        return new OrderProduct(id, orderPrice, orderCount, productId, createdAt,
                null);
    }

    /**
     * 연관관계 메서드
     */
    public void setOrderEntity(OrderEntity order) {
        this.order = order;
    }
}
