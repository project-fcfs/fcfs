package hanghae.product_service.service.common.exception;

import hanghae.product_service.controller.resp.ErrorResponse;

public class CustomApiException extends RuntimeException {

    private final ErrorResponse errorResponse;
    private final String extraMessage;

    public CustomApiException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorResponse = ErrorResponse.of(errorCode);
        this.extraMessage = null;
    }

    public CustomApiException(ErrorCode errorCode, String extraMessage) {
        super(errorCode.getMessage());
        this.errorResponse = ErrorResponse.of(errorCode);
        this.extraMessage = extraMessage;
    }

    public CustomApiException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorResponse = ErrorResponse.of(errorCode);
        this.extraMessage = null;
    }

    public ErrorResponse getErrorResponse() {
        if (extraMessage == null || !extraMessage.isBlank()) {
            return errorResponse;
        }
        return errorResponse.plusMessage(extraMessage);
    }
}
