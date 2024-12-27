package hanghae.product_service.controller;

import hanghae.product_service.controller.common.MultipartUtil;
import hanghae.product_service.controller.req.FileInfo;
import hanghae.product_service.controller.req.ProductCreateReqDto;
import hanghae.product_service.controller.resp.ProductRespDto;
import hanghae.product_service.controller.resp.ResponseDto;
import hanghae.product_service.service.ProductService;
import java.io.IOException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@RequestPart("createReqDto") ProductCreateReqDto reqDto,
                                           @RequestPart(value = "file", required = false) MultipartFile file)
            throws IOException {
        MultipartUtil.validateImageFile(file);
        FileInfo fileInfo = toFileInfo(file);
        ProductRespDto productRespDto = productService.create(reqDto.name(), reqDto.price(), reqDto.quantity(),
                fileInfo);

        return new ResponseEntity<>(productRespDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable("id") String productId) {
        ProductRespDto productRespDto = productService.getProduct(productId);
        return new ResponseEntity<>(ResponseDto.success(productRespDto), HttpStatus.OK);
    }

    private FileInfo toFileInfo(MultipartFile file) throws IOException {
        if (file == null) {
            return null;
        }
        return FileInfo.create(file);
    }

    @GetMapping
    public ResponseEntity<?> getProducts() {
        List<ProductRespDto> dtos = productService.getAllProducts();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/cart")
    public ResponseEntity<?> fetchCartProducts(@RequestParam("ids") List<String> ids) {
        List<ProductRespDto> dtos = productService.getProductByIds(ids);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }
}
