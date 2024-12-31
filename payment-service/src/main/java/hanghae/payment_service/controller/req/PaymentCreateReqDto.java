package hanghae.payment_service.controller.req;

public record PaymentCreateReqDto(
        String orderId,
        Long amount
) {
}
