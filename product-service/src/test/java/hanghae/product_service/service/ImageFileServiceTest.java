package hanghae.product_service.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import hanghae.product_service.controller.req.FileInfo;
import hanghae.product_service.domain.imagefile.ImageFile;
import hanghae.product_service.domain.product.Product;
import hanghae.product_service.domain.product.ProductStatus;
import hanghae.product_service.domain.product.ProductType;
import hanghae.product_service.mock.FakeImageFileRepository;
import hanghae.product_service.mock.FakeUuidRandomHolder;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

class ImageFileServiceTest {

    private ImageFileService imageFileService;
    private FakeImageFileRepository imageFileRepository;
    private FakeUuidRandomHolder randomHolder;

    @BeforeEach
    void setUp() {
        randomHolder = new FakeUuidRandomHolder("random");
        imageFileRepository = new FakeImageFileRepository();
        imageFileService = new ImageFileService(imageFileRepository, randomHolder);
    }

    @Test
    @DisplayName("올바른 값을 입력하면 image를 저장할 수 있다")
    void uploadImage() throws Exception {
        // given
        Product product = createProduct(1L, "product");
        String fileName = "image.jpg";
        MockMultipartFile file = createFile(fileName);
        String random = "picture";
        randomHolder.setValue(random);
        FileInfo fileInfo = FileInfo.create(file);

        // when
        imageFileService.upload(product,fileInfo);
        ImageFile result = imageFileRepository.fetchByProductId(1L).get();

        // then
        assertAll(() -> {
            assertThat(result.product().id()).isEqualTo(1L);
            assertThat(result.originalName()).isEqualTo(fileName);
            assertThat(result.storeFileName()).isEqualTo(random + ".webp");
        });
    }

    @Test
    @DisplayName("저장한 Image를 조회할 수 있다")
    void getProduct() throws Exception {
        // given
        Product product = createProduct(1L, "product");
        String fileName = "image.jpg";
        MockMultipartFile file = createFile(fileName);
        String random = "picture";
        randomHolder.setValue(random);
        FileInfo fileInfo = FileInfo.create(file);
        imageFileService.upload(product,fileInfo);

        // when
        ImageFile result = imageFileService.getImageFile(1L);

        // then
        assertAll(() -> {
            assertThat(result.product().id()).isEqualTo(1L);
            assertThat(result.originalName()).isEqualTo(fileName);
            assertThat(result.storeFileName()).isEqualTo(random + ".webp");
        });
    }

    private Product createProduct(long id, String name){
        return new Product(id, name, 10, 10, ProductType.BASIC, ProductStatus.ACTIVE);
    }

    private MockMultipartFile createFile(String originalFilename){
        return new MockMultipartFile("name", originalFilename, MediaType.IMAGE_PNG_VALUE,
                originalFilename.getBytes(StandardCharsets.UTF_8));
    }

}