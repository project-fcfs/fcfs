package hanghae.order_service.service.port;

import hanghae.order_service.controller.resp.ResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface ProductClient {

    ResponseEntity<ResponseDto<?>> isValidProduct(@PathVariable("id") String productId);
}
