package hanghae.product_service.service.lock;

import static org.assertj.core.api.Assertions.assertThat;

import hanghae.product_service.IntegrationInfraTestSupport;
import hanghae.product_service.controller.req.StockUpdateReqDto;
import hanghae.product_service.domain.product.Product;
import hanghae.product_service.domain.product.ProductType;
import hanghae.product_service.service.common.util.ProductConst;
import hanghae.product_service.service.port.ProductRepository;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RaceConditionTest extends IntegrationInfraTestSupport {

    @Autowired
    private PessimisticLockStockService pessimisticLockStockService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private NamedLockStockFacade namedLockStockFacade;

    @Autowired
    private RedissonLockStockFacade redissonLockStockFacade;

    @Autowired
    private LuaStockService luaStockService;

    @BeforeEach
    void setUp() {
        productRepository.save(Product.create("name", 100, 100, ProductType.BASIC));
    }

    @Test
    @DisplayName("비관적 락 동시성 테스트")
    void pessimisticLockTest() throws Exception {
        // given
        long startTime = System.currentTimeMillis();
        int threadCount = 100;
        ExecutorService es = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);
        Long productId = 1L;

        StockUpdateReqDto request = new StockUpdateReqDto(productId, 1);

        // when
        for (int i = 0; i < threadCount; i++) {
            es.submit(() -> {
                try {
                    List<Product> products = pessimisticLockStockService.processOrder(List.of(request));
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Product result = productRepository.findProductById(productId).get();
        long endTime = System.currentTimeMillis();

        // then
        assertThat(result.quantity()).isEqualTo(0);
        System.out.println("time taken: " + (endTime - startTime) + "ms");
    }

    @Test
    @DisplayName("네임드 락 동시성 테스트")
    @Disabled
    void NamedLockTest() throws Exception {
        // given
        long startTime = System.currentTimeMillis();
        int threadCount = 100;
        ExecutorService es = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);
        Long productId = 1L;

        StockUpdateReqDto request = new StockUpdateReqDto(productId, 1);

        // when
        for (int i = 0; i < threadCount; i++) {
            es.submit(() -> {
                try {
                    namedLockStockFacade.namedLockStock(List.of(request));
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Product result = productRepository.findProductById(productId).get();
        long endTime = System.currentTimeMillis();

        // then
        assertThat(result.quantity()).isEqualTo(0);
        System.out.println("time taken: " + (endTime - startTime) + "ms");
    }

    @Test
    @DisplayName("Redisson 락 동시성 테스트")
    @Disabled
    void RedissonLockTest() throws Exception {
        // given
        long startTime = System.currentTimeMillis();
        int threadCount = 100;
        ExecutorService es = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);
        Long productId = 1L;

        StockUpdateReqDto request = new StockUpdateReqDto(productId, 1);

        // when
        for (int i = 0; i < threadCount; i++) {
            es.submit(() -> {
                try {
                    redissonLockStockFacade.processOrder(List.of(request));
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Product result = productRepository.findProductById(productId).get();
        long endTime = System.currentTimeMillis();

        // then
        assertThat(result.quantity()).isEqualTo(0);
        System.out.println("time taken: " + (endTime - startTime) + "ms");
    }

    @Test
    @DisplayName("Lua Script  동시성 테스트")
    void LuaScriptTest() throws Exception {
        // given
        long startTime = System.currentTimeMillis();
        int threadCount = 100;
        ExecutorService es = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);
        String productId = ProductConst.PRODUCT_KEY_PREFIX + "1";
        redisTemplate.opsForHash().put(productId, "quantity", threadCount);
        StockUpdateReqDto request = new StockUpdateReqDto(1L, 1);

        // when
        for (int i = 0; i < threadCount; i++) {
            es.submit(() -> {
                try {
                    luaStockService.processOrder(List.of(request));
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Integer quantity = (Integer) redisTemplate.opsForHash().get(productId, "quantity");
        long endTime = System.currentTimeMillis();

        // then
        assertThat(quantity).isEqualTo(0);
        System.out.println("time taken: " + (endTime - startTime) + "ms");
        redisTemplate.delete(productId);
    }

}