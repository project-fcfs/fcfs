package hanghae.product_service.service.lock;

import hanghae.product_service.controller.req.StockUpdateReqDto;
import hanghae.product_service.domain.product.Product;
import hanghae.product_service.infrastrcuture.product.ProductLockRepository;
import hanghae.product_service.service.ProductStockService;
import java.util.List;
import java.util.function.Supplier;
import org.springframework.stereotype.Component;

@Component
public class NamedLockStockFacade {

    private final ProductLockRepository productLockRepository;
    private final ProductStockService productStockService;

    public NamedLockStockFacade(ProductLockRepository productLockRepository, ProductStockService productStockService) {
        this.productLockRepository = productLockRepository;
        this.productStockService = productStockService;
    }


    public List<Product> processOrder(List<StockUpdateReqDto> reqDtos) {
        List<String> productIds = reqDtos.stream()
                .map(i -> String.valueOf(i.productId())).toList();

        return (List<Product>)
                productLockRepository.executeWithLocks(productIds, () -> productStockService.processOrder(reqDtos));

    }

    public List<Product> restoreQuantity(List<StockUpdateReqDto> reqDtos) {
        List<String> productIds = reqDtos.stream()
                .map(i -> String.valueOf(i.productId())).toList();
        productLockRepository.executeWithLocks(productIds, () -> productStockService.restoreQuantity(reqDtos));
        return null;
    }
}
