package hanghae.order_service.controller.resp;

import org.springframework.http.HttpStatus;

public record ResponseDto<T>(
        int code,
        String message,
        T data,
        HttpStatus httpStatus
) {

    public static <T> ResponseDto<?> success(T data, HttpStatus httpStatus) {
        return new ResponseDto<>(1, "success", data, httpStatus);
    }

    public static <T> ResponseDto<?> fail(String message, T data, HttpStatus httpStatus) {
        return new ResponseDto<>(-1, message, data, httpStatus);
    }
}
