package hanghae.product_service.service.lock;

import hanghae.product_service.controller.req.OrderCreateReqDto;
import hanghae.product_service.service.ProductStockService;
import java.util.List;
import java.util.concurrent.TimeUnit;
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

    public void decrease(List<OrderCreateReqDto> reqDtos) {
        RLock lock = redissonClient.getLock(reqDtos.getFirst().productId());

        try{
            boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);
            if(!available){
                log.info("lock 획득 실패");
                return;
            }
            productStockService.processOrder(reqDtos);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
