package hanghae.order_service.infrastructure.product;

import hanghae.order_service.controller.resp.ResponseDto;
import hanghae.order_service.service.port.OrderProductClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("product-service")
public interface OrderOrderProductClientImpl extends OrderProductClient {

    @GetMapping("/product-service/products/{id}")
    ResponseEntity<ResponseDto<?>> isValidProduct(@PathVariable("id") String id);
}
