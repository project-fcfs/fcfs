package hanghae.order_service.infrastructure.product;


import hanghae.order_service.controller.resp.ResponseDto;
import hanghae.order_service.domain.product.Product;
import hanghae.order_service.service.port.ProductClient;
import java.util.List;
import java.util.Optional;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "product-service")
public interface ProductClientImpl extends ProductClient {

    @Override
    @GetMapping("/products/{id}")
    ResponseEntity<ResponseDto<?>> isValidProduct(@PathVariable("id") String productId);


    @GetMapping("/products/cart")
    ResponseEntity<ResponseDto<?>> getProductsByIds(@RequestParam("ids") List<String> productIds);

    @Override
    default ResponseEntity<List<Product>> getProducts(List<String> productIds){
        ResponseEntity<ResponseDto<?>> productResponse = getProductsByIds(productIds);

        List<ProductFeignResponse> feignResponses = Optional.ofNullable(productResponse.getBody())
                .map(ResponseDto::data)
                .filter(data -> data instanceof List)
                .map(data -> (List<ProductFeignResponse>) data)
                .orElseThrow(() -> new IllegalStateException("Invalid response body"));


        List<Product> productList = feignResponses.stream()
                .map(ProductFeignResponse::toModel)
                .toList();

        return new ResponseEntity<>(productList, productResponse.getStatusCode());
    };
}
