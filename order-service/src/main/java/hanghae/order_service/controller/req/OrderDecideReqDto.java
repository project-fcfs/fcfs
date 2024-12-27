package hanghae.order_service.controller.req;

import org.springframework.http.ResponseEntity;

public record OrderDecideReqDto(
        ResponseEntity<?> product,
        String orderId
) {
}
