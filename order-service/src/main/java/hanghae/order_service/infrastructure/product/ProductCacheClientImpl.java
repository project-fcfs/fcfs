package hanghae.order_service.infrastructure.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import hanghae.order_service.controller.resp.ResponseDto;
import hanghae.order_service.domain.product.Product;
import hanghae.order_service.service.common.exception.CustomApiException;
import hanghae.order_service.service.common.exception.ErrorCode;
import hanghae.order_service.service.common.util.OrderConstant;
import hanghae.order_service.service.port.ProductClient;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Primary
public class ProductCacheClientImpl implements ProductClient {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisScript<Boolean> removeStockScript;
    private final ObjectMapper mapper;
    private final String productKeyPrefix;


    public ProductCacheClientImpl(RedisTemplate<String, Object> redisTemplate, RedisScript<Boolean> removeStockScript,
                                  ObjectMapper mapper,
                                  @Value("${redis.product.prefix}") String productKeyPrefix) {
        this.redisTemplate = redisTemplate;
        this.removeStockScript = removeStockScript;
        this.mapper = mapper;
        this.productKeyPrefix = productKeyPrefix;
    }

    @Override
    public Boolean isValidProduct(Long productId) {
        String key = productKeyPrefix + productId;
        return redisTemplate.hasKey(key);
    }

    @Override
    public List<Product> processOrder(Map<Long, Integer> orderProducts) {
        List<String> productIds = new LinkedList<>();
        List<String> orderCounts = new LinkedList<>();

        for (Entry<Long, Integer> entry : orderProducts.entrySet()) {
            productIds.add(productKeyPrefix + entry.getKey());
            orderCounts.add(String.valueOf(entry.getValue()));
        }

        Boolean result = redisTemplate.execute(removeStockScript, productIds, orderCounts.toArray());
        if (!result) {
            return null;
        }
        List<Long> orderProductIds = orderProducts.keySet().stream().toList();
        return getProducts(orderProductIds);
    }

    @Override
    public List<Product> getProducts(List<Long> productIds) {
        List<Product> products = new ArrayList<>();
        List<Object> pipelineResults = redisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                for (Long productId : productIds) {
                    String key = productKeyPrefix + productId;
                    connection.hashCommands().hGetAll(key.getBytes(StandardCharsets.UTF_8));
                }
                return null;
            }
        }, redisTemplate.getHashValueSerializer());

        for (int i = 0; i < productIds.size(); i++) {
            @SuppressWarnings("unchecked")
            Map<String, Object> hash = (Map<String, Object>) pipelineResults.get(i);
            if (hash != null && !hash.isEmpty()) {
                Long productId = productIds.get(i);
                RedisProduct redisProduct = mapper.convertValue(hash, RedisProduct.class);
                products.add(redisProduct.of(productId));
            }
        }

        return products;
    }

    private record RedisProduct(
            String name,
            int price,
            int quantity
    ) {
        public Product of(Long id) {
            return new Product(id, name, price, quantity);
        }
    }
}
