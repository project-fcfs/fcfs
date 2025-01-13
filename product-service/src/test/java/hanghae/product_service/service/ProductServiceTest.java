package hanghae.product_service.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import hanghae.product_service.controller.resp.ProductRespDto;
import hanghae.product_service.domain.product.Product;
import hanghae.product_service.domain.product.ProductStatus;
import hanghae.product_service.domain.product.ProductType;
import hanghae.product_service.mock.FakeImageFileRepository;
import hanghae.product_service.mock.FakeProductCacheRepository;
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
    private FakeProductCacheRepository productCacheRepository;

    @BeforeEach
    void setUp() {
        productCacheRepository = new FakeProductCacheRepository();
        FakeImageFileRepository imageFileRepository = new FakeImageFileRepository();
        ImageFileService imageFileService = new ImageFileService(imageFileRepository, uuidRandomHolder);
        productRepository = new FakeProductRepository();
        productService = new ProductService(productRepository, productCacheRepository, imageFileService);
    }

    @Test
    @DisplayName("올바른 값을 입력하면 상품을 생성할 수 있다")
    void canSaveProduct() throws Exception {
        // given
        String name = "result";
        int price = 1000;
        int quantity = 100;
        ProductType type = ProductType.BASIC;

        // when
        productService.create(name,price,quantity, type, null);
        Product result = productRepository.findProductById(1L).get();

        // then
        assertAll(() -> {
            assertThat(result).isNotNull();
            assertThat(result.name()).isEqualTo(name);
            assertThat(result.price()).isEqualTo(price);
            assertThat(result.quantity()).isEqualTo(quantity);
            assertThat(result.type()).isEqualTo(type);
        });
    }

    @Test
    @DisplayName("저장한 상품을 조회할 수 있다")
    void canFindProduct() throws Exception {
        // given
        productService.create("product",1000,1000,ProductType.BASIC,null);

        // when
        ProductRespDto result = productService.getProduct(1L);

        // then
        assertAll(() -> {
            assertThat(result).isNotNull();
            assertThat(result.name()).isEqualTo("product");
            assertThat(result.price()).isEqualTo(1000);
        });
    }

    @Test
    @DisplayName("저장되어 있지 않은 상품을 조회할 경우 예외를 발생한다")
    void notFoundProductError() throws Exception {
        // given
        String productId = "test";

        // then
        assertThatThrownBy(() -> productService.getProduct(2L))
                .isInstanceOf(CustomApiException.class)
                .hasMessage(ErrorMessage.NOT_FOUND_PRODUCT.getMessage());
    }

    @Test
    @DisplayName("저장되어 있는 모든 상품들을 가져올 수 있다")
    void canGetNormalProducts() throws Exception {
        // given
        productRepository.save(createProduct("name1", 100, ProductType.BASIC));
        productRepository.save(createProduct("name2", 200, ProductType.BASIC));

        // when
        List<ProductRespDto> results = productService.getNormalProducts(0L, 10);

        // then
        assertThat(results).hasSize(2)
                .extracting(ProductRespDto::name, ProductRespDto::price, ProductRespDto::type)
                .containsExactlyInAnyOrder(Tuple.tuple("name1", 100, ProductType.BASIC),
                        Tuple.tuple("name2", 200, ProductType.BASIC));
    }

    @Test
    @DisplayName("Id List를 토대로 Product를 가져올 수 있다")
    void canGetNormalProductsById() throws Exception {
        // given
        ProductType type = ProductType.BASIC;
        ProductType type2 = ProductType.LIMITED;
        productRepository.save(createProduct("name1", 100, type));
        productRepository.save(createProduct("name2", 200, type2));

        // when
        List<ProductRespDto> results = productService.getProductByIds(List.of(1L, 2L));

        // then
        assertThat(results).hasSize(2)
                .extracting(ProductRespDto::name, ProductRespDto::price, ProductRespDto::type)
                .containsExactlyInAnyOrder(Tuple.tuple("name1", 100, type),
                        Tuple.tuple("name2", 200, type2));

    }

    private Product createProduct(String name, int price, ProductType type){
        return new Product(null, name, price, 10, type, ProductStatus.ACTIVE);
    }
}