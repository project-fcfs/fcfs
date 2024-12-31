package hanghae.payment_service.controller.resp;

public record ResponseDto<T>(
        int code,
        String message,
        T data
) {
}
