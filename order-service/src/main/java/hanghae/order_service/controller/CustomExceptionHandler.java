package hanghae.order_service.controller;

import hanghae.order_service.controller.resp.ResponseDto;
import hanghae.order_service.service.common.exception.CustomApiException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    private static final String INVALID_DATA_BIDING = "바인딩 에러";
    private static final Logger log = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(CustomApiException.class)
    public ResponseDto<?> handleCustomApiException(CustomApiException e) {
        log.info(e.getMessage());
        return ResponseDto.fail(e.getMessage(), null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseDto<?> validationException(MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(i -> i.getField() + ": " + i.getDefaultMessage())
                .toList();
        log.info(errors.toString());
        return ResponseDto.fail(INVALID_DATA_BIDING, errors,
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseDto<?> handleException(Exception e) {
        log.error(e.getMessage() + e.getCause());
        return ResponseDto.fail(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
