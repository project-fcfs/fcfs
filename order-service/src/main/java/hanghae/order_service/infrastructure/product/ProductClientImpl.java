package hanghae.order_service.infrastructure.product;


import hanghae.order_service.controller.resp.ResponseDto;
import hanghae.order_service.domain.order.OrderProduct;
import hanghae.order_service.domain.product.Product;
import hanghae.order_service.service.port.ProductClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "product-service")
public interface ProductClientImpl extends ProductClient {

    Logger log = LoggerFactory.getLogger(ProductClientImpl.class);

    @Override
    @GetMapping("/products/{id}")
    @Retry(name = "retryCheckProduct", fallbackMethod = "badCheckFallbackMethod")
    @CircuitBreaker(name = "circuitCheckProduct", fallbackMethod = "badCheckFallbackMethod")
    ResponseEntity<ResponseDto<?>> isValidProduct(@PathVariable("id") String productId);


    @GetMapping("/products/cart")
    @Retry(name = "retryGetProduct", fallbackMethod = "badGetFallbackMethod")
    @CircuitBreaker(name = "circuitGetProduct", fallbackMethod = "badGetFallbackMethod")
    ResponseEntity<List<ProductFeignResponse>> getProductsByIds(@RequestParam("ids") List<String> productIds);

    @Override
    default ResponseEntity<List<Product>> getProducts(List<String> productIds) {

        ResponseEntity<List<ProductFeignResponse>> productsResponse = getProductsByIds(productIds);
        List<ProductFeignResponse> data = productsResponse.getBody();

        List<Product> products = data.stream().map(ProductFeignResponse::toModel).toList();
        log.debug("getProducts: {}", products);

        return new ResponseEntity<>(products, productsResponse.getStatusCode());
    }

    @PostMapping("/products/")
    @Retry(name = "retryGetProduct", fallbackMethod = "badGetFallbackMethod")
    @CircuitBreaker(name = "circuitGetProduct", fallbackMethod = "badGetFallbackMethod")
    ResponseEntity<?> processOrder(@RequestBody List<RequestOrder> requestOrders);

    @Override
    default ResponseEntity<?> removeStock(List<OrderProduct> orderProducts) {
        List<RequestOrder> request = orderProducts.stream().map(i -> new RequestOrder(i.productId(), i.orderPrice()))
                .toList();
        return processOrder(request);
    }

    default ResponseEntity<?> badCheckFallbackMethod(String productId, Throwable t){
        log.error("Bad Check fallback error {}, id -> {}", t.getMessage(), productId);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    default ResponseEntity<?> badGetFallbackMethod(List<String> productIds, Throwable t){
        log.error("Bad Get fallback method error {} ids -> {}", t.getMessage(), productIds);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    record RequestOrder(
            String productId,
            Integer orderPrice
    ) {}

}
