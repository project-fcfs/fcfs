package hanghae.payment_service.service.port;

import hanghae.payment_service.domain.Payment;
import java.util.Optional;

public interface PaymentRepository {

    Payment save(Payment payment);
    Optional<Payment> findByOrderId(String orderId);
}
