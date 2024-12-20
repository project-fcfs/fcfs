package hanghae.product_service.controller;

import hanghae.product_service.controller.common.MultipartUtil;
import hanghae.product_service.controller.req.FileInfo;
import hanghae.product_service.controller.req.ProductCreateReqDto;
import hanghae.product_service.service.ProductService;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/product/create")
    public ResponseEntity<?> createProduct(@RequestPart ProductCreateReqDto createReqDto,
                                           @RequestPart MultipartFile file) throws IOException {
        MultipartUtil.validateImageFile(file);
        FileInfo fileInfo = toFileInfo(file);
        productService.create(createReqDto, fileInfo);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public FileInfo toFileInfo(MultipartFile file) throws IOException {
        if(file == null) return null;
        return FileInfo.create(file);
    }
}
