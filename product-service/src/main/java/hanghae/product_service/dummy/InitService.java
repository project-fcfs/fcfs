package hanghae.product_service.dummy;

import hanghae.product_service.domain.product.ProductType;
import hanghae.product_service.service.ProductService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class InitService {

    private final ProductService productService;

    public InitService(ProductService productService) {
        this.productService = productService;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void normalInit() {
        for (int i = 0; i < 500; i++) {
            productService.create("name"+i, 4000, 200_000, ProductType.BASIC,null);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void LimitedInit() {
        productService.create("맥북 m1", 4000, 10, ProductType.LIMITED,null);
        productService.create("맥북 m2", 4000, 20, ProductType.LIMITED,null);
    }
}
