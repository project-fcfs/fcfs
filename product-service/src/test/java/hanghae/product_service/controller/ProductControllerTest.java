/*
package hanghae.product_service.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.core.JsonProcessingException;
import hanghae.product_service.IntegrationInfraTestSupport;
import hanghae.product_service.controller.req.ProductCreateReqDto;
import hanghae.product_service.domain.product.Product;
import hanghae.product_service.domain.product.ProductType;
import hanghae.product_service.service.port.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

class ProductControllerTest extends IntegrationInfraTestSupport {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("API를 호출해 상품을 저장할 수 있다")
    void canSaveProduct() throws Exception {
        // given
        ProductCreateReqDto request = new ProductCreateReqDto("product", 1000, 10, ProductType.BASIC);
        MockMultipartFile reqDtoPart = createFile(request);
        MockMultipartFile filePart = createFile();

        // when
        ResultActions resultActions = mockMvc.perform(multipart("/products/create")
                        .file(reqDtoPart)
                        .file(filePart)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andDo(print());

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @DisplayName("API를 호출해 저장한 상품을 조회할 수 있다")
    void canFindProduct() throws Exception {
        // given
        ProductType type = ProductType.BASIC;
        String name = "product";
        int price = 1000;
        int quantity = 10;
        productRepository.save(Product.create(name, price, quantity, type));

        // when
        ResultActions resultActions = mockMvc.perform(get("/products/{id}", 1))
                .andDo(print());

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.data.name").value(name))
                .andExpect(jsonPath("$.data.price").value(price))
                .andExpect(jsonPath("$.data.quantity").value(quantity))
        ;

    }

    private MockMultipartFile createFile() {
        return new MockMultipartFile(
                "file",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "dummy image content".getBytes()
        );
    }

    private MockMultipartFile createFile(ProductCreateReqDto request) throws JsonProcessingException {
        return new MockMultipartFile("createReqDto", "reqDto.json", MediaType.APPLICATION_JSON_VALUE,
                mapper.writeValueAsBytes(request));
    }

}*/
