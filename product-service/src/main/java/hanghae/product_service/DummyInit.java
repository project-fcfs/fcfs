package hanghae.product_service;

import hanghae.product_service.domain.product.ProductType;
import hanghae.product_service.service.ProductService;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class DummyInit {

    private final ProductService productService;

    public DummyInit(ProductService productService) {
        this.productService = productService;
    }

    @PostConstruct
    public void init() {
        productService.create("name1", 1000, 10, ProductType.LIMITED, null);
        productService.create("name4", 4000, 100_000, ProductType.BASIC,null);
    }
}
