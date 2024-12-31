package hanghae.payment_service.domain;

import java.time.LocalDateTime;

public record Payment(
        Long id,
        String orderId,
        PaymentStatus status,
        Long amount,
        String paymentId,
        LocalDateTime createdAt
) {
    public static Payment create(String orderId, PaymentStatus status, Long amount, String paymentId, LocalDateTime createdAt) {
        return new Payment(null, orderId, status, amount, paymentId, createdAt);
    }
}
