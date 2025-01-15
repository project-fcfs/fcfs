package hanghae.product_service.service.lock;

import hanghae.product_service.controller.req.StockUpdateReqDto;
import hanghae.product_service.domain.product.Product;
import hanghae.product_service.service.ProductStockService;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RedissonLockStockFacade {

    private static final Logger log = LoggerFactory.getLogger(RedissonLockStockFacade.class);
    private final RedissonClient redissonClient;
    private final ProductStockService productStockService;

    public RedissonLockStockFacade(RedissonClient redissonClient, ProductStockService productStockService) {
        this.redissonClient = redissonClient;
        this.productStockService = productStockService;
    }

    public List<Product> processOrder(List<StockUpdateReqDto> reqDtos) {
        List<RLock> locks = reqDtos.stream()
                .map(product -> redissonClient.getLock("lock:" + product.productId()))
                .toList();
        RLock[] lockArray = locks.toArray(new RLock[0]);

        RedissonMultiLock lock = new RedissonMultiLock(lockArray);

        try {
            boolean available = lock.tryLock(30, 5, TimeUnit.SECONDS);
            if (!available) {
                log.info("lock 획득 실패");
                return null;
            }
            return productStockService.processOrder(reqDtos);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public List<Product> restoreQuantity(List<StockUpdateReqDto> reqDtos) {
        List<RLock> locks = reqDtos.stream()
                .map(product -> redissonClient.getLock("lock:" + product.productId()))
                .toList();
        RLock[] lockArray = locks.toArray(new RLock[0]);

        RedissonMultiLock multiLock = new RedissonMultiLock(lockArray);

        try {
            boolean available = multiLock.tryLock(10, 5, TimeUnit.SECONDS);
            if (!available) {
                log.info("lock 획득 실패");
                return null;
            }
            return productStockService.restoreQuantity(reqDtos);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            multiLock.unlock();
        }
    }
}
