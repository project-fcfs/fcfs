package hanghae.order_service.infrastructure.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import hanghae.order_service.domain.product.Product;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ProductFeignResponse(
        Long productId,
        String name,
        Integer price,
        Integer quantity
) {

    public Product toModel() {
        return new Product(productId, name, price, quantity);
    }
}
