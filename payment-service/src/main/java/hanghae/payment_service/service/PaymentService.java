package hanghae.payment_service.service;

import static hanghae.payment_service.service.common.util.PaymentConst.FAIL;
import static hanghae.payment_service.service.common.util.PaymentConst.SUCCESS;

import hanghae.payment_service.domain.Payment;
import hanghae.payment_service.domain.PaymentStatus;
import hanghae.payment_service.service.port.LocalDateTimeHolder;
import hanghae.payment_service.service.port.OrderMessage;
import hanghae.payment_service.service.port.PaymentRepository;
import hanghae.payment_service.service.port.UuidRandomHolder;
import java.time.LocalDateTime;
import java.util.Random;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final LocalDateTimeHolder localDateTimeHolder;
    private final UuidRandomHolder uuidRandomHolder;
    private final OrderMessage orderMessage;

    public PaymentService(PaymentRepository paymentRepository, LocalDateTimeHolder localDateTimeHolder,
                          UuidRandomHolder uuidRandomHolder, OrderMessage orderMessage) {
        this.paymentRepository = paymentRepository;
        this.localDateTimeHolder = localDateTimeHolder;
        this.uuidRandomHolder = uuidRandomHolder;
        this.orderMessage = orderMessage;
    }

    public Payment create(String orderId, Long amount) {
        LocalDateTime currentDate = localDateTimeHolder.getCurrentDate();
        String paymentId = uuidRandomHolder.getRandomUuid();
        int random = new Random().nextInt(10);

        PaymentStatus paymentStatus = random > 2 ? PaymentStatus.SUCCESS : PaymentStatus.FAIL;
        int code = paymentStatus.equals(PaymentStatus.SUCCESS) ? SUCCESS.getCode() : FAIL.getCode();
        String messageDescription =
                paymentStatus.equals(PaymentStatus.SUCCESS) ? SUCCESS.getDescription() : FAIL.getDescription();

        return processPayment(orderId, amount, paymentId, currentDate, paymentStatus, code, messageDescription);
    }

    private Payment processPayment(String orderId, Long amount, String paymentId, LocalDateTime currentDate,
                                   PaymentStatus paymentStatus, int code, String messageDescription) {
        Payment payment = Payment.create(orderId, paymentStatus, amount, paymentId, currentDate);
        Payment savedPayment = paymentRepository.save(payment);
        orderMessage.sendOrderDecide(code, messageDescription, orderId);
        return savedPayment;
    }
}
