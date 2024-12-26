package hanghae.product_service;

import hanghae.product_service.service.ProductService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DummyInit {

    private final ProductService productService;

    public DummyInit(ProductService productService) {
        this.productService = productService;
    }

    @PostConstruct
    public void init() {
        productService.create("name1", 1000, 10, null);
        productService.create("name2", 2000, 20, null);
        productService.create("name3", 3000, 30, null);
        productService.create("name4", 4000, 100_000, null);
    }
}
