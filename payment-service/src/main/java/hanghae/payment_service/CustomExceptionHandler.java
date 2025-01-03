package hanghae.payment_service;

import hanghae.payment_service.controller.resp.ResponseDto;
import hanghae.payment_service.service.common.exception.CustomApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(CustomApiException.class)
    public ResponseDto<?> customApiException(CustomApiException e) {
        log.info(e.getMessage());
        return ResponseDto.fail(e.getMessage(), null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseDto<?> customException(Exception e) {
        log.error(e.getMessage());
        return ResponseDto.fail(e.getMessage(), null, HttpStatus.BAD_REQUEST);
    }
}
