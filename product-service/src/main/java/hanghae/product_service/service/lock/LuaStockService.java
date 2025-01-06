package hanghae.product_service.service.lock;

import static hanghae.product_service.service.common.util.ProductConst.PRODUCT_KEY_PREFIX;

import hanghae.product_service.controller.req.OrderCreateReqDto;
import hanghae.product_service.controller.req.OrderMessageReqDto;
import hanghae.product_service.service.port.ProductCacheRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LuaStockService {

    private final ProductCacheRepository productCacheRepository;

    public LuaStockService(ProductCacheRepository productCacheRepository) {
        this.productCacheRepository = productCacheRepository;
    }


    public Boolean processOrder(List<OrderCreateReqDto> reqDtos) {

        List<String> productIds = reqDtos.stream().map(i -> PRODUCT_KEY_PREFIX + i.productId()).toList();
        List<String> orderCounts = reqDtos.stream().map(i -> String.valueOf(i.orderCount())).toList();

        return productCacheRepository.removeStock(productIds, orderCounts);

    }


    public Boolean restoreQuantity(List<OrderMessageReqDto> reqDtos) {
        List<String> productIds = reqDtos.stream().map(i -> PRODUCT_KEY_PREFIX + i.productId()).toList();
        List<String> orderCounts = reqDtos.stream().map(i -> String.valueOf(i.orderCount())).toList();

        return productCacheRepository.removeStock(productIds, orderCounts);
    }

}
