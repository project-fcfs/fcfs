package hanghae.product_service;

import hanghae.product_service.service.ProductService;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
public class DummyInit {

    private final ProductService productService;

    public DummyInit(ProductService productService) {
        this.productService = productService;
    }

    @PostConstruct
    public void init() {
        productService.create("name1", 1000, 10, null);
        productService.create("name4", 4000, 100_000, null);
    }
}
