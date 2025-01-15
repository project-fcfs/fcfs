package hanghae.order_service.controller;

import hanghae.order_service.controller.resp.ErrorResponse;
import hanghae.order_service.service.common.exception.CustomApiException;
import hanghae.order_service.service.common.exception.CustomKafkaException;
import hanghae.order_service.service.common.exception.ErrorCode;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(CustomApiException.class)
    public ResponseEntity<ErrorResponse> handleCustomApiException(final CustomApiException e) {
        log.info(e.getMessage());
        return ResponseEntity.badRequest()
                .body(ErrorResponse.of(e.getErrorCode()));
    }

    @ExceptionHandler(CustomKafkaException.class)
    public ResponseEntity<String> handleCustomKafkaException(final CustomKafkaException e){
        log.warn(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validationException(final MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(i -> i.getField() + ": " + i.getDefaultMessage())
                .toList();
        log.info(errors.toString());
        ErrorCode errorCode = ErrorCode.INVALID_DATA_BINDING;
        ErrorResponse response = new ErrorResponse(errorCode.getCode(), errors.toString());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(final Exception e) {
        log.error(e.getMessage() + e.getCause());
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), errorCode.getMessage());
        return ResponseEntity.internalServerError()
                .body(errorResponse);
    }
}
