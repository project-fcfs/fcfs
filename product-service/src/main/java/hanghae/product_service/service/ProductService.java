package hanghae.product_service.service;

import hanghae.product_service.controller.req.FileInfo;
import hanghae.product_service.controller.resp.ProductRespDto;
import hanghae.product_service.domain.imagefile.ImageFile;
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
    public ProductRespDto create(String name, int price, int quantity, FileInfo fileInfo) {
        Product product = Product.create(name, price, quantity, uuidRandomHolder.getRandomUUID());

        Product savedProduct = productRepository.save(product);
        ImageFile imageFile = imageFileService.upload(savedProduct, fileInfo);
        return ProductRespDto.of(savedProduct, imageFile);
    }

    public ProductRespDto getProduct(String productUid) {
        Product product = retrieveProductByUid(productUid);
        ImageFile imageFile = imageFileService.getImageFile(product.id());
        return ProductRespDto.of(product, imageFile);
    }

    @Transactional
    public void order(String productUid, Integer count) {
        Product product = retrieveProductByUid(productUid);
        product.removeStock(count);
    }

    private Product retrieveProductByUid(String productUid) {
        return productRepository.fetchByUid(productUid).orElseThrow(() ->
                new CustomApiException(ErrorMessage.NOT_FOUND_PRODUCT.getMessage()));
    }
    
}
