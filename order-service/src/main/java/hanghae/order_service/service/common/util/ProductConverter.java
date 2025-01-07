package hanghae.order_service.service.common.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hanghae.order_service.infrastructure.product.ProductFeignResponse;
import java.util.List;

public class ProductConverter {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> List<ProductFeignResponse> convertProductResponse(T data) {
        return mapper.convertValue(data,
                new TypeReference<List<ProductFeignResponse>>() {
                });
    }
}
