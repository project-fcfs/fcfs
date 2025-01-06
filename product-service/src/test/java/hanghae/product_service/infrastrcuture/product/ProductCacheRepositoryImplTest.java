package hanghae.product_service.infrastrcuture.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import hanghae.product_service.IntegrationInfraTestSupport;
import hanghae.product_service.controller.req.OrderCreateReqDto;
import hanghae.product_service.domain.product.Product;
import hanghae.product_service.domain.product.ProductStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.script.RedisScript;

class ProductCacheRepositoryImplTest extends IntegrationInfraTestSupport {
    private static final String KEY_PREFIX = "product:";

    @Autowired
    private ProductCacheRepositoryImpl productCacheRepository;

    @Autowired
    private RedisScript<Boolean> removeStockScript;

    @Test
    @DisplayName("Product를 Redis로 Hash형태로 저장을 할 수 있다")
    void canSaveHashProduct() throws Exception {
        // given
        String productId = "productId";
        Product product = new Product(1L, "name", 1000, 200, productId, ProductStatus.ACTIVE);

        // when
        productCacheRepository.save(product);

        // then
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(KEY_PREFIX + productId);
        assertAll(() -> {
            assertThat(entries).hasSize(6)
                    .containsEntry("productId", productId)
                    .containsEntry("quantity", "200");
        });
        redisTemplate.delete(KEY_PREFIX + productId);
    }

    @Test
    @DisplayName("Lua Script 테스트")
    void LuaScript_Test() throws Exception {
        // given
        String key = KEY_PREFIX + 1;
        String key2 = KEY_PREFIX + 2;
        String quantity = "quantity";
        redisTemplate.opsForHash().put(key, quantity, 3);
        redisTemplate.opsForHash().put(key2, quantity, 3);
        List<String> keys = List.of(key, key2);
        List<String> args = List.of("2", "1");

        // when
        Boolean result = redisTemplate.execute(removeStockScript, keys, args.toArray());
        Object quantity1 = redisTemplate.opsForHash().get(key, quantity);
        Object quantity2 = redisTemplate.opsForHash().get(key2, quantity);

        // then
        System.out.println("quantity1 = " + quantity1);
        System.out.println("quantity2 = " + quantity2);
        System.out.println("result = " + result);
        redisTemplate.delete(key);
        redisTemplate.delete(key2);
    }

    @Test
    @DisplayName("Lua Script를 통해 여러 Product의 재고를 감소시킬 수 있다")
    void canLuaScriptRemoveStock() throws Exception {
        // given
        List<OrderCreateReqDto> dtos = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            String productId = "productId" + i;
            Product product = new Product((long) i, "name" + i, 1000 + i, 3, productId, ProductStatus.ACTIVE);
            productCacheRepository.save(product);
            dtos.add(new OrderCreateReqDto(KEY_PREFIX+ productId, i));
        }

        List<String> productIds = dtos.stream().map(OrderCreateReqDto::productId).toList();
        List<String> orderCounts = dtos.stream().map(i -> String.valueOf(i.orderCount())).toList();

        // when
        Boolean result = productCacheRepository.removeStock(productIds, orderCounts);

        // then
        for (int i = 0; i < 3; i++) {
            Map<Object, Object> entries = redisTemplate.opsForHash().entries(KEY_PREFIX + "productId" + i);
            System.out.println(i + "번째 entries = " + entries);
            redisTemplate.delete(KEY_PREFIX + "productId" + i);
        }
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Lua Script를 통해 여러 Product의 재고를 감소도중 하나라도 0미만이 된다면 취소된다")
    void canLuaScriptRemoveStockRollback() throws Exception {
        // given
        List<OrderCreateReqDto> dtos = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            String productId = "productId" + i;
            Product product = new Product((long) i, "name" + i, 1000 + i, 3, productId, ProductStatus.ACTIVE);
            productCacheRepository.save(product);
            dtos.add(new OrderCreateReqDto(KEY_PREFIX+ productId, i + 2));
        }

        List<String> productIds = dtos.stream().map(OrderCreateReqDto::productId).toList();
        List<String> orderCounts = dtos.stream().map(i -> String.valueOf(i.orderCount())).toList();

        // when
        Boolean result = productCacheRepository.removeStock(productIds, orderCounts);

        // then
        for (int i = 0; i < 3; i++) {
            Map<Object, Object> entries = redisTemplate.opsForHash().entries(KEY_PREFIX + "productId" + i);
            System.out.println(i + "번째 entries = " + entries);
            redisTemplate.delete(KEY_PREFIX + "productId" + i);
        }
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Lua Script를 통해 여러 Product의 재고를 증가시킨다")
    void canRestoreStockLua() throws Exception {
        // given
        List<OrderCreateReqDto> dtos = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            String productId = "productId" + i;
            Product product = new Product((long) i, "name" + i, 1000 + i, 3, productId, ProductStatus.ACTIVE);
            productCacheRepository.save(product);
            dtos.add(new OrderCreateReqDto(KEY_PREFIX+ productId, i + 2));
        }

        List<String> productIds = dtos.stream().map(OrderCreateReqDto::productId).toList();
        List<String> orderCounts = dtos.stream().map(i -> String.valueOf(i.orderCount())).toList();

        // when
        Boolean result = productCacheRepository.restoreStock(productIds, orderCounts);

        // then
        for (int i = 0; i < 3; i++) {
            Map<Object, Object> entries = redisTemplate.opsForHash().entries(KEY_PREFIX + "productId" + i);
            Integer quantity = (Integer) entries.get("quantity");
            assertThat(quantity).isEqualTo(3 + (i + 2));
            System.out.println(i + "번째 entries = " + entries);
            redisTemplate.delete(KEY_PREFIX + "productId" + i);
        }
        assertThat(result).isTrue();
    }
}