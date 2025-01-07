package hanghae.product_service.service;

import hanghae.product_service.controller.req.FileInfo;
import hanghae.product_service.controller.resp.ProductRespDto;
import hanghae.product_service.domain.imagefile.ImageFile;
import hanghae.product_service.domain.product.Product;
import hanghae.product_service.domain.product.ProductType;
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
    private final ImageFileService imageFileService;

    public ProductService(ProductRepository productRepository, ImageFileService imageFileService) {
        this.productRepository = productRepository;
        this.imageFileService = imageFileService;
    }

    /**
     * 상품 생성
     */
    @Transactional
    public ProductRespDto create(String name, int price, int quantity, ProductType productType, FileInfo fileInfo) {
        Product product = Product.create(name, price, quantity, productType);

        Product savedProduct = productRepository.save(product);
        ImageFile imageFile = imageFileService.upload(savedProduct, fileInfo);
        return ProductRespDto.of(savedProduct, imageFile);
    }

    /**
     * productId로 하나의 상품을 찾고 해당 상품의 썸네일과 함께 반환한다
     */
    public ProductRespDto getProduct(Long productId) {
        Product product = fetchProductById(productId);
        ImageFile imageFile = imageFileService.getImageFile(product.id());
        return ProductRespDto.of(product, imageFile);
    }

    /**
     * productId로 하나의 상품을 찾고 없으면 예외를 반환한다
     */
    private Product fetchProductById(Long productId) {
        return productRepository.findProductById(productId).orElseThrow(() ->
                new CustomApiException(ErrorMessage.NOT_FOUND_PRODUCT.getMessage()));
    }

    /**
     * DB에 모든 Product를 반환한다
     */
    public List<ProductRespDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return assembleProductAndImage(products);
    }

    /**
     * 여러 Product들을 ProductIds로 찾는다
     */
    public List<ProductRespDto> getProductByIds(List<Long> ids) {
        List<Product> products = productRepository.findAllByProductIds(ids);
        return assembleProductAndImage(products);
    }

    /**
     * 여러 Product에 맞는 이미지 썸네일을 찾아서 같이 반환한다
     */
    private List<ProductRespDto> assembleProductAndImage(List<Product> products) {
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
