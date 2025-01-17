package hanghae.product_service.controller;

import hanghae.product_service.controller.common.MultipartUtil;
import hanghae.product_service.controller.req.FileInfo;
import hanghae.product_service.controller.req.ProductCreateReqDto;
import hanghae.product_service.controller.req.StockUpdateReqDto;
import hanghae.product_service.controller.resp.ProductRespDto;
import hanghae.product_service.domain.product.Product;
import hanghae.product_service.service.ProductService;
import hanghae.product_service.service.common.exception.CustomApiException;
import hanghae.product_service.service.common.exception.ErrorCode;
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
public class ProductController implements ProductControllerDocs {

    private final ProductService productService;
    private final PessimisticLockStockService pessimisticLockStockService;
    private final RedissonLockStockFacade redissonLockStockFacade;
    private final NamedLockStockFacade namedLockStockFacade;

    public ProductController(ProductService productService,
                             PessimisticLockStockService pessimisticLockStockService,
                             RedissonLockStockFacade redissonLockStockFacade,
                             NamedLockStockFacade namedLockStockFacade) {
        this.productService = productService;
        this.pessimisticLockStockService = pessimisticLockStockService;
        this.redissonLockStockFacade = redissonLockStockFacade;
        this.namedLockStockFacade = namedLockStockFacade;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@RequestPart("createReqDto") ProductCreateReqDto reqDto,
                                           @RequestPart(value = "file", required = false) MultipartFile file) {
        MultipartUtil.validateImageFile(file);
        FileInfo fileInfo = toFileInfo(file);
        ProductRespDto productRespDto = productService.create(reqDto.name(), reqDto.price(), reqDto.quantity(),
                reqDto.type(), fileInfo);

        return new ResponseEntity<>(productRespDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable("id") Long productId) {
        ProductRespDto productRespDto = productService.getProduct(productId);
        return new ResponseEntity<>(productRespDto, HttpStatus.OK);
    }

    private FileInfo toFileInfo(MultipartFile file) {
        if (file == null) {
            return null;
        }
        try {
            return FileInfo.create(file);
        } catch (IOException e) {
            throw new CustomApiException(ErrorCode.ERROR_PARSE_DATA, file.toString());
        }
    }

    @GetMapping
    public ResponseEntity<?> getProducts(@RequestParam(value = "cursor", defaultValue = "0") Long cursor,
                                         @RequestParam(value = "size", defaultValue = "10") int size) {
        List<ProductRespDto> dtos = productService.getNormalProducts(cursor, size);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/fcfs")
    public ResponseEntity<?> getFcfsProducts(@RequestParam(value = "cursor", defaultValue = "0") Long cursor,
                                             @RequestParam(value = "size", defaultValue = "10") int size) {
        List<ProductRespDto> dtos = productService.getFcfsProducts(cursor, size);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/cart")
    public ResponseEntity<?> fetchProductsById(@RequestParam("ids") List<Long> ids) {
        List<ProductRespDto> dtos = productService.getProductByIds(ids);

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping("/order")
    public ResponseEntity<?> processOrder(@RequestBody List<StockUpdateReqDto> dtos) {
        List<Product> products = pessimisticLockStockService.processOrder(dtos);
        List<ProductRespDto> response = products.stream().map(i -> ProductRespDto.of(i, null)).toList();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
