package hanghae.payment_service.infrastructure;

import hanghae.payment_service.domain.Payment;
import hanghae.payment_service.domain.PaymentStatus;
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
@Table(name = "payment")
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String orderId;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    @Column(nullable = false)
    private Long amount;
    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected PaymentEntity() {
    }

    public PaymentEntity(Long id, String orderId, PaymentStatus status, Long amount, LocalDateTime createdAt) {
        this.id = id;
        this.orderId = orderId;
        this.status = status;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public static PaymentEntity toModel(Payment payment) {
        return new PaymentEntity(payment.id(), payment.orderId(), payment.status(), payment.amount(), payment.createdAt());
    }

    public Payment toModel(){
        return new Payment(id, orderId, status, amount, createdAt);
    }
}
