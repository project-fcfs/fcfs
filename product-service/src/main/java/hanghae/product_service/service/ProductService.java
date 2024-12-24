package hanghae.product_service.service;

import hanghae.product_service.controller.req.FileInfo;
import hanghae.product_service.controller.resp.ProductRespDto;
import hanghae.product_service.domain.imagefile.ImageFile;
import hanghae.product_service.domain.product.Product;
import hanghae.product_service.service.common.exception.CustomApiException;
import hanghae.product_service.service.common.util.ErrorMessage;
import hanghae.product_service.service.port.ProductRepository;
import hanghae.product_service.service.port.UUIDRandomHolder;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
        Product product = fetchProductById(productUid);
        ImageFile imageFile = imageFileService.getImageFile(product.id());
        return ProductRespDto.of(product, imageFile);
    }

    @Transactional
    public void processOrder(String productId, Integer count) {
        Product product = fetchProductById(productId);
        product.removeStock(count);
    }

    private Product fetchProductById(String productId) {
        return productRepository.findProductById(productId).orElseThrow(() ->
                new CustomApiException(ErrorMessage.NOT_FOUND_PRODUCT.getMessage()));
    }

    public List<ProductRespDto>  getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<Long> productIds = products.stream().map(Product::id).toList();
        List<ImageFile> imageFiles = imageFileService.getAllInProductId(productIds);

        Map<Long, ImageFile> map = imageFiles.stream().collect(Collectors.toMap(k -> k.product().id(), v -> v));

        return products.stream()
                .map(i -> {
                    ImageFile imageFile = map.get(i.id());
                    return ProductRespDto.of(i, imageFile);
                }).toList();
    }
}
