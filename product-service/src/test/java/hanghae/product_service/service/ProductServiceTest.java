package hanghae.product_service.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import hanghae.product_service.controller.resp.ProductRespDto;
import hanghae.product_service.domain.product.Product;
import hanghae.product_service.domain.product.ProductStatus;
import hanghae.product_service.mock.FakeImageFileRepository;
import hanghae.product_service.mock.FakeProductRepository;
import hanghae.product_service.mock.FakeUuidRandomHolder;
import hanghae.product_service.service.common.exception.CustomApiException;
import hanghae.product_service.service.common.util.ErrorMessage;
import java.util.List;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductServiceTest {

    private ProductService productService;
    private FakeUuidRandomHolder uuidRandomHolder;
    private FakeProductRepository productRepository;

    @BeforeEach
    void setUp() {
        uuidRandomHolder = new FakeUuidRandomHolder("random");
        FakeImageFileRepository imageFileRepository = new FakeImageFileRepository();
        ImageFileService imageFileService = new ImageFileService(imageFileRepository, uuidRandomHolder);
        productRepository = new FakeProductRepository();
        productService = new ProductService(productRepository, uuidRandomHolder, imageFileService);
    }

    @Test
    @DisplayName("올바른 값을 입력하면 상품을 생성할 수 있다")
    void canSaveProduct() throws Exception {
        // given
        String name = "result";
        int price = 1000;
        int quantity = 100;
        String productId = "create";
        uuidRandomHolder.setValue(productId);

        // when
        productService.create(name,price,quantity,null);
        Product result = productRepository.findProductById(productId).get();

        // then
        assertAll(() -> {
            assertThat(result).isNotNull();
            assertThat(result.name()).isEqualTo(name);
            assertThat(result.price()).isEqualTo(price);
            assertThat(result.quantity()).isEqualTo(quantity);
            assertThat(result.productId()).isEqualTo(productId);
        });
    }

    @Test
    @DisplayName("저장한 상품을 조회할 수 있다")
    void canFindProduct() throws Exception {
        // given
        String productId = "find";
        uuidRandomHolder.setValue(productId);
        productService.create("product",1000,1000,null);

        // when
        ProductRespDto result = productService.getProduct(productId);

        // then
        assertAll(() -> {
            assertThat(result).isNotNull();
            assertThat(result.name()).isEqualTo("product");
            assertThat(result.productId()).isEqualTo(productId);
        });
    }

    @Test
    @DisplayName("저장되어 있지 않은 상품을 조회할 경우 예외를 발생한다")
    void notFoundProductError() throws Exception {
        // given
        String productId = "test";

        // then
        assertThatThrownBy(() -> productService.getProduct(productId))
                .isInstanceOf(CustomApiException.class)
                .hasMessage(ErrorMessage.NOT_FOUND_PRODUCT.getMessage());
    }

    @Test
    @DisplayName("저장되어 있는 모든 상품들을 가져올 수 있다")
    void canGetAllProducts() throws Exception {
        // given
        productRepository.save(createProduct("name1", 100, "productId1"));
        productRepository.save(createProduct("name2", 200, "productId2"));

        // when
        List<ProductRespDto> results = productService.getAllProducts();

        // then
        assertThat(results).hasSize(2)
                .extracting(ProductRespDto::name, ProductRespDto::price, ProductRespDto::productId)
                .containsExactlyInAnyOrder(Tuple.tuple("name1", 100, "productId1"),
                        Tuple.tuple("name2", 200, "productId2"));
    }

    @Test
    @DisplayName("Id List를 토대로 Product를 가져올 수 있다")
    void canGetAllProductsById() throws Exception {
        // given
        String productId1 = "productId1";
        String productId2 = "productId2";
        productRepository.save(createProduct("name1", 100, productId1));
        productRepository.save(createProduct("name2", 200, productId2));

        // when
        List<ProductRespDto> results = productService.getProductByIds(List.of(productId1, productId2));

        // then
        assertThat(results).hasSize(2)
                .extracting(ProductRespDto::name, ProductRespDto::price, ProductRespDto::productId)
                .containsExactlyInAnyOrder(Tuple.tuple("name1", 100, "productId1"),
                        Tuple.tuple("name2", 200, "productId2"));

    }

    private Product createProduct(String name, int price, String productId){
        return new Product(null, name, price, 10, productId, ProductStatus.ACTIVE);
    }
}