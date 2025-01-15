package hanghae.order_service.service.common.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hanghae.order_service.infrastructure.product.ProductFeignResponse;
import hanghae.order_service.service.common.exception.CustomApiException;
import hanghae.order_service.service.common.exception.ErrorCode;
import java.io.IOException;
import java.util.List;

public class ProductConverter {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<ProductFeignResponse> convertProductResponse(Object data) {
        try {
            return mapper.readValue((String) data, new TypeReference<List<ProductFeignResponse>>() {
            });
        } catch (IOException e) {
            throw new CustomApiException(ErrorCode.ERROR_PARSE_DATA, e);
        }

    }
}
