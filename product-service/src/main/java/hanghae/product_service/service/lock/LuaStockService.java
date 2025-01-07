package hanghae.product_service.service.lock;

import hanghae.product_service.controller.req.StockUpdateReqDto;
import hanghae.product_service.domain.product.Product;
import hanghae.product_service.service.port.ProductCacheRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LuaStockService {

    private final ProductCacheRepository productCacheRepository;
    private final PessimisticLockStockService pessimisticLockStockService;
    private final String productKeyPrefix;

    public LuaStockService(ProductCacheRepository productCacheRepository,
                           PessimisticLockStockService pessimisticLockStockService,
                           @Value("${redis.product.prefix}") String productKeyPrefix) {
        this.productCacheRepository = productCacheRepository;
        this.pessimisticLockStockService = pessimisticLockStockService;
        this.productKeyPrefix = productKeyPrefix;
    }

    @Transactional
    public List<Product> processOrder(List<StockUpdateReqDto> reqDtos) {
        return pessimisticLockStockService.processOrder(reqDtos);
    }

    @Transactional
    public List<Product> restoreQuantity(List<StockUpdateReqDto> reqDtos) {
        List<String> productIds = reqDtos.stream().map(i -> productKeyPrefix + i.productId()).toList();
        List<String> orderCounts = reqDtos.stream().map(i -> String.valueOf(i.orderCount())).toList();

        productCacheRepository.restoreStock(productIds, orderCounts);
        return pessimisticLockStockService.restoreQuantity(reqDtos);
    }

}
