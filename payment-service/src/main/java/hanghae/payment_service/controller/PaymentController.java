package hanghae.payment_service.controller;

import hanghae.payment_service.controller.req.PaymentCreateReqDto;
import hanghae.payment_service.domain.Payment;
import hanghae.payment_service.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController implements PaymentControllerDocs{

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<?> processPayment(@Valid @RequestBody PaymentCreateReqDto reqDto) {
        Payment payment = paymentService.create(reqDto.orderId(), reqDto.amount());
        return ResponseEntity.ok(payment);
    }
}
