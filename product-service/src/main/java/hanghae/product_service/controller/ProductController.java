package hanghae.product_service.controller;

import hanghae.product_service.controller.common.MultipartUtil;
import hanghae.product_service.controller.req.FileInfo;
import hanghae.product_service.controller.req.StockUpdateReqDto;
import hanghae.product_service.controller.req.ProductCreateReqDto;
import hanghae.product_service.controller.resp.ProductRespDto;
import hanghae.product_service.controller.resp.ResponseDto;
import hanghae.product_service.domain.product.Product;
import hanghae.product_service.service.ProductService;
import hanghae.product_service.service.lock.NamedLockStockFacade;
import hanghae.product_service.service.lock.PessimisticLockStockService;
import hanghae.product_service.service.lock.RedissonLockStockFacade;
import java.io.IOException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final PessimisticLockStockService pessimisticLockStockService;
    private final RedissonLockStockFacade redissonLockStockFacade;
    private final NamedLockStockFacade namedLockStockFacade;

    public ProductController(ProductService productService,
                             PessimisticLockStockService pessimisticLockStockService,
                             RedissonLockStockFacade redissonLockStockFacade, NamedLockStockFacade namedLockStockFacade) {
        this.productService = productService;
        this.pessimisticLockStockService = pessimisticLockStockService;
        this.redissonLockStockFacade = redissonLockStockFacade;
        this.namedLockStockFacade = namedLockStockFacade;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@RequestPart("createReqDto") ProductCreateReqDto reqDto,
                                           @RequestPart(value = "file", required = false) MultipartFile file)
            throws IOException {
        MultipartUtil.validateImageFile(file);
        FileInfo fileInfo = toFileInfo(file);
        ProductRespDto productRespDto = productService.create(reqDto.name(), reqDto.price(), reqDto.quantity(),
                reqDto.type(), fileInfo);

        return new ResponseEntity<>(productRespDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseDto<?> getProduct(@PathVariable("id") Long productId) {
        ProductRespDto productRespDto = productService.getProduct(productId);
        return ResponseDto.success(productRespDto, HttpStatus.OK);
    }

    private FileInfo toFileInfo(MultipartFile file) throws IOException {
        if (file == null) {
            return null;
        }
        return FileInfo.create(file);
    }

    @GetMapping
    public ResponseDto<?> getProducts() {
        List<ProductRespDto> dtos = productService.getAllProducts();
        return ResponseDto.success(dtos, HttpStatus.OK);
    }

    @GetMapping("/cart")
    public ResponseDto<?> fetchCartProducts(@RequestParam("ids") List<Long> ids) {
        List<ProductRespDto> dtos = productService.getProductByIds(ids);
        return ResponseDto.success(dtos, HttpStatus.OK);
    }

    @PostMapping("/order")
    public ResponseDto<?> processOrder(@RequestBody List<StockUpdateReqDto> dtos) {
        List<Product> products = namedLockStockFacade.processOrder(dtos);
        List<ProductRespDto> response = products.stream().map(i -> ProductRespDto.of(i, null)).toList();
        return ResponseDto.success(response, HttpStatus.OK);
    }
}
