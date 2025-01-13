/*
package hanghae.order_service.infrastructure.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.fasterxml.jackson.core.type.TypeReference;
import hanghae.order_service.controller.resp.ResponseDto;
import hanghae.order_service.domain.product.Product;
import hanghae.order_service.service.common.util.OrderConstant;
import hanghae.order_service.support.IntegrationTestSupport;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ProductCacheClientImplTest extends IntegrationTestSupport {

    @Autowired
    private ProductCacheClientImpl productCacheClient;

    @Test
    @DisplayName("캐시서버에 상품이 있다면 True를 반환한다")
    void ifGetProductTrue() throws Exception {
        // given
        Long productId = 1L;
        String prefix = env.getProperty("redis.product.prefix");
        String key = prefix + productId;
        redisTemplate.opsForHash().put(key,"quantity", 30);

        // when
        Boolean result = productCacheClient.isValidProduct(productId);

        // then
        assertThat(result).isTrue();
        redisTemplate.delete(key);
    }

    @Test
    @DisplayName("캐시서버에 상품이 없다면 False 반환한다")
    void ifNotGetProductTrue() throws Exception {
        // given
        Long productId = 1L;

        // when
        Boolean result = productCacheClient.isValidProduct(productId);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Cache 서버에 있는 상품정보를 가져올 수 있다")
    void canFetchProduct() throws Exception {
        // given
        Long key1 = 1L;
        Long key2 = 2L;
        Integer quantity = 5;

        String prefix = env.getProperty("redis.product.prefix");
        Product product1 = new Product(1L, "name", 1000, quantity);
        Product product2 = new Product(2L, "test", 1234, quantity);

        saveProduct(prefix + key1, product1);
        saveProduct(prefix + key2, product2);

        // when
        List<Product> products = productCacheClient.getProducts(List.of(1L, 2L));

        // then
        assertThat(products).hasSize(2)
                .extracting(Product::productId, Product::name, Product::price, Product::quantity)
                .containsExactlyInAnyOrder(Tuple.tuple(1L, "name", 1000, quantity),
                        Tuple.tuple(2L, "test", 1234, quantity))
        ;
        Object test = redisTemplate.opsForValue().get("test");
        System.out.println("test = " + test);
        redisTemplate.delete(prefix + key1);
        redisTemplate.delete(prefix + key2);
    }

    @Test
    @DisplayName("캐시서버에 수량이 있다면 주문하고 상품 정보를 가져올 수 있다")
    void canBringTheCacheData() throws Exception {
        // given
        Long key1 = 1L;
        Long key2 = 2L;
        Integer quantity = 5;
        Integer orderCount = 3;

        String prefix = env.getProperty("redis.product.prefix");
        Product product1 = new Product(1L, "name", 1000, quantity);
        Product product2 = new Product(2L, "test", 1234, quantity);

        saveProduct(prefix + key1, product1);
        saveProduct(prefix + key2, product2);

        Map<Long, Integer> orderProducts = new HashMap<>();
        orderProducts.put(key1, orderCount);
        orderProducts.put(key2, orderCount);

        // when
        ResponseDto<List<Product>> response = productCacheClient.processOrder(orderProducts);

        // then
        assertAll(() -> {
            assertThat(response.code()).isEqualTo(OrderConstant.ORDER_SUCCESS);
            assertThat(response.data()).hasSize(2)
                    .extracting(Product::productId, Product::name, Product::price, Product::quantity)
                    .containsExactlyInAnyOrder(Tuple.tuple(1L, "name", 1000, quantity - orderCount),
                            Tuple.tuple(2L, "test", 1234, quantity - orderCount));
        });
        redisTemplate.delete(prefix + key1);
        redisTemplate.delete(prefix + key2);
    }

    @Test
    @DisplayName("캐시서버에 수량이 없다면 주문할 수 없다")
    void cannotBringTheCacheData() throws Exception {
        // given
        Long key1 = 1L;
        Long key2 = 2L;
        Integer quantity = 5;
        Integer orderCount = 6;

        String prefix = env.getProperty("redis.product.prefix");
        Product product1 = new Product(1L, "name", 1000, quantity);
        Product product2 = new Product(2L, "test", 1234, quantity);

        saveProduct(prefix + key1, product1);
        saveProduct(prefix + key2, product2);

        Map<Long, Integer> orderProducts = new HashMap<>();
        orderProducts.put(key1, orderCount);
        orderProducts.put(key2, orderCount);

        // when
        ResponseDto<List<Product>> response = productCacheClient.processOrder(orderProducts);

        // then
        assertAll(() -> {
            assertThat(response.code()).isEqualTo(OrderConstant.ORDER_FAIL);
        });
        redisTemplate.delete(prefix + key1);
        redisTemplate.delete(prefix + key2);
    }

    private void saveProduct(String key, Product product) {
        Map<String, String> value = mapper.convertValue(product, new TypeReference<Map<String, String>>() {
        });
        redisTemplate.opsForHash().putAll(key, value);
    }

}*/
