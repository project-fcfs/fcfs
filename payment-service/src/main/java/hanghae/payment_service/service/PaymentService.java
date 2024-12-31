package hanghae.payment_service.service;

import hanghae.payment_service.domain.Payment;
import hanghae.payment_service.domain.PaymentStatus;
import hanghae.payment_service.service.port.LocalDateTimeHolder;
import hanghae.payment_service.service.port.PaymentRepository;
import java.time.LocalDateTime;
import java.util.Random;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final LocalDateTimeHolder localDateTimeHolder;

    public PaymentService(PaymentRepository paymentRepository, LocalDateTimeHolder localDateTimeHolder) {
        this.paymentRepository = paymentRepository;
        this.localDateTimeHolder = localDateTimeHolder;
    }

    public Payment create(String orderId, Long amount){
        LocalDateTime currentDate = localDateTimeHolder.getCurrentDate();
        int random = new Random().nextInt(10);
        Payment payment;
        // todo 결제 성공 및 취소 시 각자 관련 로직 실행
        if (random > 2) {
            payment = Payment.create(orderId, PaymentStatus.SUCCESS, amount, currentDate);
        } else{
            payment = Payment.create(orderId, PaymentStatus.FAIL, amount, currentDate);
        }
        return paymentRepository.save(payment);
    }
}
