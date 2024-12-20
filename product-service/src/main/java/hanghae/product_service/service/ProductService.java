package hanghae.product_service.service;

import hanghae.product_service.controller.req.FileInfo;
import hanghae.product_service.controller.req.ProductCreateReqDto;
import hanghae.product_service.domain.product.Product;
import hanghae.product_service.service.common.exception.CustomApiException;
import hanghae.product_service.service.common.util.ErrorMessage;
import hanghae.product_service.service.port.ProductRepository;
import hanghae.product_service.service.port.UUIDRandomHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final UUIDRandomHolder uuidRandomHolder;
    private final ImageFileService imageFileService;

    public ProductService(ProductRepository productRepository,
                          UUIDRandomHolder uuidRandomHolder, ImageFileService imageFileService) {
        this.productRepository = productRepository;
        this.uuidRandomHolder = uuidRandomHolder;
        this.imageFileService = imageFileService;
    }

    @Transactional
    public void create(ProductCreateReqDto reqDto, FileInfo fileInfo) {
        Product product = Product.create(reqDto.name(), reqDto.price(),
                reqDto.quantity(), uuidRandomHolder.getRandomUUID());

        Product savedProduct = productRepository.save(product);
        imageFileService.upload(savedProduct, fileInfo);
    }

    public Product getProduct(String productUid) {
        return productRepository.fetchByUid(productUid).orElseThrow(() ->
            new CustomApiException(ErrorMessage.NOT_FOUND_PRODUCT.getMessage()));
    }
}
