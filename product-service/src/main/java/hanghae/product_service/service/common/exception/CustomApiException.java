package hanghae.product_service.service.common.exception;

import hanghae.product_service.controller.resp.ErrorResponse;

public class CustomApiException extends RuntimeException {

    private final ErrorResponse errorResponse;

    public CustomApiException(ErrorCode errorCode, String value) {
        super(errorCode.getMessage());
        this.errorResponse = ErrorResponse.of(errorCode, value);
    }

    public CustomApiException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorResponse = ErrorResponse.of(errorCode, null);
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }
}
