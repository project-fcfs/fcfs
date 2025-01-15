package hanghae.product_service.controller.resp;


import hanghae.product_service.service.common.exception.ErrorCode;

public record ErrorResponse(
        int code,
        String message
) {

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getCode(), errorCode.getMessage());
    }

    public ErrorResponse plusMessage(String message) {
        return new ErrorResponse(this.code, this.message + ", " + message);
    }
}
