package hanghae.payment_service.mock;

import hanghae.payment_service.domain.Payment;
import hanghae.payment_service.service.port.PaymentRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class FakePaymentRepository implements PaymentRepository {
    private List<Payment> data = new ArrayList<>();
    private AtomicLong counter = new AtomicLong();
    @Override
    public Payment save(Payment payment) {
        if(payment.id() == null || payment.id() == 0L){
            Payment newPayment = new Payment(counter.incrementAndGet(),
                    payment.orderId(), payment.status(),
                    payment.amount(), payment.paymentId(), payment.createdAt());
            data.add(newPayment);
            return newPayment;
        }else{
            data.removeIf(i -> i.id().equals(payment.id()));
            data.add(payment);
            return payment;
        }
    }

    public Optional<Payment> findById(long id) {
        return data.stream().filter(i -> i.id().equals(id)).findFirst();
    }
}
