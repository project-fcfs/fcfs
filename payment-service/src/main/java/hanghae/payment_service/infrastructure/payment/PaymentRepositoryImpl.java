package hanghae.payment_service.infrastructure.payment;

import hanghae.payment_service.domain.Payment;
import hanghae.payment_service.service.port.PaymentRepository;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentRepositoryImpl implements PaymentRepository {
    private final PaymentJpaRepository jpaRepository;

    public PaymentRepositoryImpl(PaymentJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Payment save(Payment payment) {
        return jpaRepository.save(PaymentEntity.toModel(payment)).toModel();
    }
}
