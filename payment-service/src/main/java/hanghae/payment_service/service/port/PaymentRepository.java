package hanghae.payment_service.service.port;

import hanghae.payment_service.domain.Payment;

public interface PaymentRepository {

    Payment save(Payment payment);
}
