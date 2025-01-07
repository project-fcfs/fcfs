package hanghae.product_service.service.lock;

import hanghae.product_service.controller.req.StockUpdateReqDto;
import hanghae.product_service.service.port.ProductCacheRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LuaStockService {

    private final ProductCacheRepository productCacheRepository;
    private final String productKeyPrefix;

    public LuaStockService(ProductCacheRepository productCacheRepository,
                           @Value("${redis.product.prefix}") String productKeyPrefix) {
        this.productCacheRepository = productCacheRepository;
        this.productKeyPrefix = productKeyPrefix;
    }


    public Boolean processOrder(List<StockUpdateReqDto> reqDtos) {

        List<String> productIds = reqDtos.stream().map(i -> productKeyPrefix + i.productId()).toList();
        List<String> orderCounts = reqDtos.stream().map(i -> String.valueOf(i.orderCount())).toList();

        return productCacheRepository.removeStock(productIds, orderCounts);
    }


    public Boolean restoreQuantity(List<StockUpdateReqDto> reqDtos) {
        List<String> productIds = reqDtos.stream().map(i -> productKeyPrefix + i.productId()).toList();
        List<String> orderCounts = reqDtos.stream().map(i -> String.valueOf(i.orderCount())).toList();

        return productCacheRepository.removeStock(productIds, orderCounts);
    }

}
