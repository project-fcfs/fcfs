package hanghae.product_service.controller;

import hanghae.product_service.controller.resp.ErrorResponse;
import hanghae.product_service.service.common.exception.CustomApiException;
import hanghae.product_service.service.common.exception.ErrorCode;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(CustomApiException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomApiException e) {
        log.info("custom api Exception {}, {}", e.getMessage(), e.getErrorResponse());
        ErrorResponse errorResponse = e.getErrorResponse();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status,
                                                                  WebRequest request) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(i -> i.getField() + ": " + i.getDefaultMessage())
                .toList();

        String message = String.join("\n", errors);
        log.debug("바인딩 에러 {} ", message);
        ErrorCode errorCode = ErrorCode.INVALID_DATA_BINDING;
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), errorCode.getMessage(), message);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e){
        log.warn("[Exception] 예상치 못한 오류 {} 가 발생했습니다. \n", e.getClass().getName(), e);
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(ErrorResponse.of(errorCode), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
