package hanghae.order_service.infrastructure.product;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hanghae.order_service.controller.resp.ResponseDto;
import hanghae.order_service.domain.product.Product;
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

    @Override
    @GetMapping("/products/{id}")
    @Retry(name = "retryCheckProduct", fallbackMethod = "badCheckFallbackMethod")
    @CircuitBreaker(name = "circuitCheckProduct", fallbackMethod = "badCheckFallbackMethod")
    ResponseEntity<ResponseDto<?>> isValidProduct(@PathVariable("id") String productId);

    default ResponseEntity<?> badCheckFallbackMethod(String productId, Throwable t) {
        log.error("Bad Check fallback error {}, id -> {}", t.getMessage(), productId);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    default ResponseEntity<?> badGetFallbackMethod(List<String> productIds, Throwable t) {
        log.error("Bad Get fallback method error {} ids -> {}", t.getMessage(), productIds);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/products/order")
    @Retry(name = "retryGetProduct", fallbackMethod = "processOrderRetryFallbackMethod")
    @CircuitBreaker(name = "circuitGetProduct", fallbackMethod = "processOrderCircuitFallbackMethod")
    ResponseDto<?> removeStock(@RequestBody List<RequestOrder> requestOrders);

    @Override
    default ResponseDto<List<Product>> processOrder(Map<String, Integer> cartProducts) {
        List<RequestOrder> request = cartProducts.entrySet().stream()
                .map(i -> new RequestOrder(i.getKey(), i.getValue()))
                .toList();
        ResponseDto<?> responseDto = removeStock(request);
        if(responseDto.code() == -1){
            return new ResponseDto<>(responseDto.code(), responseDto.message(), null, responseDto.httpStatus());
        }
        List<ProductFeignResponse> responses = new ObjectMapper().convertValue(responseDto.data(),
                new TypeReference<List<ProductFeignResponse>>() {});

        List<Product> products = responses.stream().map(ProductFeignResponse::toModel).toList();
        return new ResponseDto<>(responseDto.code(), responseDto.message(), products, responseDto.httpStatus());
    }

    default ResponseDto<?> processOrderCircuitFallbackMethod(List<RequestOrder> requestOrders, Throwable t) {
        log.error("processOrder circuit fallback error {}, request -> {}", t.getMessage(), requestOrders);
        return ResponseDto.fail("processOrder circuit fallback", null, HttpStatus.BAD_GATEWAY);
    }

    default ResponseDto<?> processOrderRetryFallbackMethod(List<RequestOrder> requestOrders, Throwable t) {
        log.error("processOrder retry fallback method error {} request -> {}", t.getMessage(), requestOrders);
        return ResponseDto.fail("processOrder retry fallback", null, HttpStatus.BAD_GATEWAY);
    }

    @GetMapping("/products/cart")
    @Retry(name = "retryGetProduct", fallbackMethod = "getIdsRetryFallbackMethod")
    @CircuitBreaker(name = "circuitGetProduct", fallbackMethod = "getIdsCircuitFallbackMethod")
    ResponseEntity<List<ProductFeignResponse>> getProductsByIds(@RequestParam("ids") List<String> productIds);

    @Override
    default ResponseEntity<List<Product>> getProducts(List<String> productIds) {

        ResponseEntity<List<ProductFeignResponse>> productsResponse = getProductsByIds(productIds);
        List<ProductFeignResponse> data = productsResponse.getBody();

        List<Product> products = data.stream().map(ProductFeignResponse::toModel).toList();
        log.debug("getProducts: {}", products);

        return new ResponseEntity<>(products, productsResponse.getStatusCode());
    }

    default ResponseEntity<?> getIdsCircuitFallbackMethod(List<String> productIds, Throwable t) {
        log.error("get Ids circuit fallback error {}, request -> {}", t.getMessage(), productIds);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    default ResponseEntity<?> getIdsRetryFallbackMethod(List<String> productIds, Throwable t) {
        log.error("get Ids retry fallback method error {} request -> {}", t.getMessage(), productIds);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    record RequestOrder(
            String productId,
            Integer orderCount
    ) {
    }

}
