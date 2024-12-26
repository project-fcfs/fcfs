package hanghae.order_service.infrastructure.product;

import hanghae.order_service.controller.resp.ResponseDto;
import hanghae.order_service.service.port.ProductClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service")
public interface ProductClientImpl extends ProductClient {

    @Override
    @GetMapping("/products/{id}")
    ResponseEntity<ResponseDto<?>> isValidProduct(@PathVariable("id") String productId);
}
