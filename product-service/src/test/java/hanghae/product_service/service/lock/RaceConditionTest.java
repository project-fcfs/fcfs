package hanghae.product_service.service.lock;

import static org.assertj.core.api.Assertions.assertThat;

import hanghae.product_service.IntegrationInfraTestSupport;
import hanghae.product_service.controller.req.OrderCreateReqDto;
import hanghae.product_service.domain.product.Product;
import hanghae.product_service.service.port.ProductRepository;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    void setUp() {
        productRepository.save(Product.create("name", 100, 100, "productId"));
    }

    @Test
    @DisplayName("비관적 락 동시성 테스트")
    void pessimisticLockTest() throws Exception {
        // given
        long startTime = System.currentTimeMillis();
        int threadCount = 100;
        ExecutorService es = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);
        String productId = "productId";

        OrderCreateReqDto request = new OrderCreateReqDto(productId, 1);

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
        System.out.println("time taken: " + (endTime - startTime) +"ms");
    }

    @Test
    @DisplayName("네임드 락 동시성 테스트")
    void NamedLockTest() throws Exception {
        // given
        long startTime = System.currentTimeMillis();
        int threadCount = 100;
        ExecutorService es = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);
        String productId = "productId";

        OrderCreateReqDto request = new OrderCreateReqDto(productId, 1);

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
        System.out.println("time taken: " + (endTime - startTime) +"ms");
    }

    @Test
    @DisplayName("Redisson 락 동시성 테스트")
    void RedissondLockTest() throws Exception {
        // given
        long startTime = System.currentTimeMillis();
        int threadCount = 100;
        ExecutorService es = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);
        String productId = "productId";

        OrderCreateReqDto request = new OrderCreateReqDto(productId, 1);

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
        System.out.println("time taken: " + (endTime - startTime) +"ms");
    }



}