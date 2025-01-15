package hanghae.order_service.infrastructure.product;


import feign.FeignException;
import hanghae.order_service.domain.product.Product;
import hanghae.order_service.service.common.exception.CustomApiException;
import hanghae.order_service.service.common.exception.ErrorCode;
import hanghae.order_service.service.common.util.ProductConverter;
import hanghae.order_service.service.port.ProductClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import java.util.List;
import java.util.Map;
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

    @GetMapping("/products/{id}")
    @Retry(name = "retryCheckProduct", fallbackMethod = "badCheckFallbackMethod")
    @CircuitBreaker(name = "circuitCheckProduct", fallbackMethod = "badCheckFallbackMethod")
    ResponseEntity<?> checkProduct(@PathVariable("id") Long productId);

    @Override
    default Boolean isValidProduct(Long productId) {
        try {
            ResponseEntity<?> response = checkProduct(productId);
            log.debug("is Valid product {} ", response);
            return response.getStatusCode().equals(HttpStatus.OK);
        } catch (FeignException e) {
            throw new CustomApiException(ErrorCode.INVALID_REQUEST, e);
        }
    }

    default ResponseEntity<?> badCheckFallbackMethod(Long productId, Throwable t) {
        log.warn("Bad Check fallback error {}, id -> {}", t.getMessage(), productId);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    default ResponseEntity<?> badGetFallbackMethod(Long productId, Throwable t) {
        log.warn("Bad Get fallback method error {} ids -> {}", t.getMessage(), productId);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/products/order")
    @Retry(name = "retryRemoveStock", fallbackMethod = "processOrderRetryFallbackMethod")
    @CircuitBreaker(name = "circuitRemoveStock", fallbackMethod = "processOrderCircuitFallbackMethod")
    ResponseEntity<?> removeStock(@RequestBody List<RequestOrder> requestOrders);

    @Override
    default List<Product> processOrder(Map<Long, Integer> cartProducts) {
        List<RequestOrder> request = cartProducts.entrySet().stream()
                .map(i -> new RequestOrder(i.getKey(), i.getValue()))
                .toList();

        ResponseEntity<?> response = removeStock(request);
        if (!response.getStatusCode().is2xxSuccessful()) {
            return null;
        }
        log.debug("process order data {} ", response);
        List<ProductFeignResponse> data = ProductConverter.convertProductResponse(response.getBody());

        return data.stream().map(ProductFeignResponse::toModel).toList();


    }

    default ResponseEntity<?> processOrderCircuitFallbackMethod(List<RequestOrder> requestOrders, Throwable t) {
        log.error("processOrder circuit fallback error {}, request -> {}", t.getMessage(), requestOrders);
        return ResponseEntity.badRequest().build();
    }

    default ResponseEntity<?> processOrderRetryFallbackMethod(List<RequestOrder> requestOrders, Throwable t) {
        log.error("processOrder retry fallback method error {} request -> {}", t.getMessage(), requestOrders);
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/products/cart")
    @Retry(name = "retryGetProduct", fallbackMethod = "getIdsRetryFallbackMethod")
    @CircuitBreaker(name = "circuitGetProduct", fallbackMethod = "getIdsCircuitFallbackMethod")
    ResponseEntity<?> getProductsByIds(@RequestParam("ids") List<Long> productIds);

    @Override
    default List<Product> getProducts(List<Long> productIds) {

        ResponseEntity<?> response = getProductsByIds(productIds);
        if (!response.getStatusCode().is2xxSuccessful()) {
            return null;
        }
        List<ProductFeignResponse> data = ProductConverter.convertProductResponse(
                response.getBody());
        return data.stream().map(ProductFeignResponse::toModel).toList();


    }

    default ResponseEntity<?> getIdsCircuitFallbackMethod(List<Long> productIds, Throwable t) {
        log.error("get Ids circuit fallback error {}, request -> {}", t.getMessage(), productIds);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    default ResponseEntity<?> getIdsRetryFallbackMethod(List<Long> productIds, Throwable t) {
        log.error("get Ids retry fallback method error {} request -> {}", t.getMessage(), productIds);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    record RequestOrder(
            Long productId,
            Integer orderCount
    ) {
    }

}
