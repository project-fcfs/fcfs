package hanghae.product_service.controller;

import hanghae.product_service.controller.resp.ResponseDto;
import hanghae.product_service.service.common.exception.CustomApiException;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseDto<?> validationException(MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(i -> i.getField() + ": " + i.getDefaultMessage())
                .toList();

        return ResponseDto.fail(errors, INVALID_DATA_BIDING, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseDto<?> illegalArgumentException(IllegalArgumentException e) {
        log.info(e.getMessage());
        return ResponseDto.fail(null, e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomApiException.class)
    public ResponseDto<?> customApiException(CustomApiException e) {
        log.info(e.getMessage());
        return ResponseDto.fail(null, e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
