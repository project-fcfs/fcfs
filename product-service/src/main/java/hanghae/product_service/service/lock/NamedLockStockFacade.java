package hanghae.product_service.service.lock;

import hanghae.product_service.controller.req.StockUpdateReqDto;
import hanghae.product_service.infrastrcuture.product.NamedLockTemplate;
import hanghae.product_service.service.ProductStockService;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class NamedLockStockFacade {

    private final NamedLockTemplate namedLockTemplate;
    private final ProductStockService productStockService;

    public NamedLockStockFacade(NamedLockTemplate namedLockTemplate, ProductStockService productStockService) {
        this.namedLockTemplate = namedLockTemplate;
        this.productStockService = productStockService;
    }

    @Transactional
    public void namedLockStock(List<StockUpdateReqDto> reqDtos) {

        namedLockTemplate.executeWithLock("lock", 3, () -> productStockService.processOrder(reqDtos));

    }
}
