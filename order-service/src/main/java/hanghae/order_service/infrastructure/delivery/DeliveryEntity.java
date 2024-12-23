package hanghae.order_service.infrastructure.delivery;

import hanghae.order_service.domain.order.Delivery;
import hanghae.order_service.domain.order.DeliveryStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "delivery")
public class DeliveryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String address;
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    protected DeliveryEntity() {
    }

    public DeliveryEntity(Long id, String address, DeliveryStatus status, LocalDateTime createdAt,
                          LocalDateTime updatedAt) {
        this.id = id;
        this.address = address;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static DeliveryEntity fromModel(Delivery delivery) {
        return new DeliveryEntity(delivery.id(), delivery.address(), delivery.status(),
                delivery.createdAt(), delivery.updatedAt());
    }

    public Delivery toModel(){
        return new Delivery(id, address, status, createdAt, updatedAt);
    }
}
