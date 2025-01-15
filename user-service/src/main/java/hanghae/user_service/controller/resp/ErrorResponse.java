package hanghae.user_service.controller.resp;


import hanghae.user_service.service.common.exception.ErrorCode;

public record ErrorResponse(
        int code,
        String message,
        String data
) {

    public static ErrorResponse of(ErrorCode errorCode, String data) {
        return new ErrorResponse(errorCode.getCode(), errorCode.getMessage(), data);
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getCode(), errorCode.getMessage(), null);
    }
}