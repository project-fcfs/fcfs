package hanghae.product_service.service.lock;

import hanghae.product_service.controller.req.StockUpdateReqDto;
import hanghae.product_service.domain.product.Product;
import hanghae.product_service.service.ProductStockService;
import hanghae.product_service.service.port.StockRepository;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class NamedLockStockFacade {

    private final StockRepository stockRepository;
    private final ProductStockService productStockService;

    public NamedLockStockFacade(StockRepository stockRepository, ProductStockService productStockService) {
        this.stockRepository = stockRepository;
        this.productStockService = productStockService;
    }

    public List<Product> processOrder(List<StockUpdateReqDto> reqDtos) {
        List<String> productIds = reqDtos.stream()
                .map(i -> String.valueOf(i.productId())).toList();
        try {
            stockRepository.getLock(productIds);
            return productStockService.processOrder(reqDtos);
        } finally {
            stockRepository.releaseLock(productIds);
        }
    }

    public List<Product> restoreQuantity(List<StockUpdateReqDto> reqDtos) {
        List<String> productIds = reqDtos.stream()
                .map(i -> String.valueOf(i.productId())).toList();
        try {
            stockRepository.getLock(productIds);
            return productStockService.processOrder(reqDtos);
        } finally {
            stockRepository.releaseLock(productIds);
        }
    }
}
