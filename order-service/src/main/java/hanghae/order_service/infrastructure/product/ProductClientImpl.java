package hanghae.order_service.infrastructure.product;


import feign.FeignException;
import hanghae.order_service.controller.resp.ResponseDto;
import hanghae.order_service.domain.product.Product;
import hanghae.order_service.service.common.exception.CustomApiException;
import hanghae.order_service.service.common.util.OrderConstant;
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
    ResponseDto<?> checkProduct(@PathVariable("id") Long productId);

    @Override
    default Boolean isValidProduct(Long productId) {
        try {
            ResponseDto<?> response = checkProduct(productId);
            return response.code() == OrderConstant.ORDER_SUCCESS;
        } catch (FeignException e) {
            throw new CustomApiException(e.getMessage(), e);
        }

    }

    default ResponseEntity<?> badCheckFallbackMethod(Long productId, Throwable t) {
        log.error("Bad Check fallback error {}, id -> {}", t.getMessage(), productId);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    default ResponseEntity<?> badGetFallbackMethod(Long productId, Throwable t) {
        log.error("Bad Get fallback method error {} ids -> {}", t.getMessage(), productId);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/products/order")
    @Retry(name = "retryGetProduct", fallbackMethod = "processOrderRetryFallbackMethod")
    @CircuitBreaker(name = "circuitGetProduct", fallbackMethod = "processOrderCircuitFallbackMethod")
    ResponseDto<?> removeStock(@RequestBody List<RequestOrder> requestOrders);

    @Override
    default ResponseDto<List<Product>> processOrder(Map<Long, Integer> cartProducts) {
        List<RequestOrder> request = cartProducts.entrySet().stream()
                .map(i -> new RequestOrder(i.getKey(), i.getValue()))
                .toList();
        try {
            ResponseDto<?> response = removeStock(request);
            if (response.code() == -1) {
                return new ResponseDto<>(response.code(), response.message(), null, response.httpStatus());
            }
            List<ProductFeignResponse> data = ProductConverter.convertProductResponse(response.data());

            List<Product> products = data.stream().map(ProductFeignResponse::toModel).toList();
            return new ResponseDto<>(response.code(), response.message(), products, response.httpStatus());
        } catch (FeignException e) {
            throw new CustomApiException(e.getMessage(), e);
        }

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
    ResponseDto<?> getProductsByIds(@RequestParam("ids") List<Long> productIds);

    @Override
    default List<Product> getProducts(List<Long> productIds) {

        try {
            ResponseDto<?> response = getProductsByIds(productIds);

            List<ProductFeignResponse> data = ProductConverter.convertProductResponse(response.data());

            return data.stream().map(ProductFeignResponse::toModel).toList();
        } catch (FeignException e) {
            throw new CustomApiException(e.getMessage(), e);
        }

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
