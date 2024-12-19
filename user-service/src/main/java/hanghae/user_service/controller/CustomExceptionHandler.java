package hanghae.user_service.controller;

import static hanghae.user_service.service.common.util.ErrorMessage.INVALID_DATA_BINDING;

import hanghae.user_service.controller.resp.ResponseDto;
import hanghae.user_service.service.common.exception.CustomApiException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(CustomApiException.class)
    public ResponseEntity<?> handleCustomApiException(CustomApiException e) {

        log.info(e.getMessage());
        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleBindException(MethodArgumentNotValidException e) {

        List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(i -> i.getField() + ": " + i.getDefaultMessage())
                .toList();

        return new ResponseEntity<>(new ResponseDto<>(-1, INVALID_DATA_BINDING.getMessage(), errors),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), null), HttpStatus.BAD_REQUEST);
    }
}
