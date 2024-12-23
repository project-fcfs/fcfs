package hanghae.product_service.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import hanghae.product_service.controller.resp.ProductRespDto;
import hanghae.product_service.domain.product.Product;
import hanghae.product_service.mock.FakeImageFileRepository;
import hanghae.product_service.mock.FakeProductRepository;
import hanghae.product_service.mock.FakeUuidRandomHolder;
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
    void createProduct() throws Exception {
        // given
        String name = "result";
        int price = 1000;
        int quantity = 100;
        String uid = "create";
        uuidRandomHolder.setValue(uid);

        // when
        productService.create(name,price,quantity,null);
        Product result = productRepository.fetchByUid(uid).get();

        // then
        assertAll(() -> {
            assertThat(result).isNotNull();
            assertThat(result.name()).isEqualTo(name);
            assertThat(result.price()).isEqualTo(price);
            assertThat(result.quantity()).isEqualTo(quantity);
            assertThat(result.productId()).isEqualTo(uid);
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
}