package hanghae.product_service.infrastrcuture.product;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hanghae.product_service.domain.product.Product;
import hanghae.product_service.domain.product.ProductStatus;
import hanghae.product_service.domain.product.ProductType;
import hanghae.product_service.service.port.ProductCacheRepository;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Repository;

@Repository
public class ProductCacheRepositoryImpl implements ProductCacheRepository {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final RedisScript<Boolean> removeStockScript;
    private final RedisScript<Boolean> restoreStockScript;
    private final String productKeyPrefix;

    public ProductCacheRepositoryImpl(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper,
                                      RedisScript<Boolean> removeStockScript, RedisScript<Boolean> restoreStockScript,
                                      @Value("${redis.product.prefix}") String productKeyPrefix) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.removeStockScript = removeStockScript;
        this.restoreStockScript = restoreStockScript;
        this.productKeyPrefix = productKeyPrefix;
    }

    @Override
    public void save(Product product) {
        Map<String, String> productMap = convertToMap(product);
        String key = productKeyPrefix + product.id();
        redisTemplate.opsForHash().putAll(key, productMap);
        redisTemplate.opsForHash().put(key, "quantity", product.quantity());
    }

    private Map<String, String> convertToMap(Product product) {
        RedisProduct redisProduct = RedisProduct.of(product);
        try {
            return objectMapper.convertValue(redisProduct, new TypeReference<Map<String, String>>() {
            });
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Failed to convert product to map", e);
        }
    }

    @Override
    public Boolean removeStock(List<String> productIds, List<String> orderCounts) {

        return redisTemplate.execute(removeStockScript, productIds, orderCounts.toArray());
    }

    @Override
    public Boolean restoreStock(List<String> productIds, List<String> orderCounts) {

        return redisTemplate.execute(restoreStockScript, productIds, orderCounts.toArray());
    }

    private record RedisProduct(
            String name,
            int price,
            int quantity,
            ProductType type,
            ProductStatus productStatus
    ) {
        public static RedisProduct of(Product product) {
            return new RedisProduct(product.name(), product.price(), product.quantity(),
                    product.type(),
                    product.status());
        }
    }
}
