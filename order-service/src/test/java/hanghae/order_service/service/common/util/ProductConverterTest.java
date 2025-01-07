package hanghae.order_service.service.common.util;

import static org.assertj.core.api.Assertions.assertThat;

import hanghae.order_service.controller.resp.ResponseDto;
import hanghae.order_service.domain.product.Product;
import hanghae.order_service.domain.product.Product.ProductStatus;
import hanghae.order_service.infrastructure.product.ProductFeignResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class ProductConverterTest {

    @Test
    @DisplayName("Open Feign으로 넘어오는 데이터를 ProductFeignResponse으로 바꿀 수 있다")
    void convertData() throws Exception {
        // given
        Product product = new Product("name", 1000, 3000, "productId", ProductStatus.ACTIVE, "image");
        ResponseDto<?> responseDto = new ResponseDto<>(1, "success", List.of(product), HttpStatus.OK);

        // when
        List<ProductFeignResponse> result = ProductConverter.convertProductResponse(responseDto.data());

        // then
        assertThat(result).hasSize(1);
        System.out.println("result = " + result);
    }

}