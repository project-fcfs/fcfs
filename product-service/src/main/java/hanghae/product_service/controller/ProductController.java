package hanghae.product_service.controller;

import hanghae.product_service.controller.common.MultipartUtil;
import hanghae.product_service.controller.req.FileInfo;
import hanghae.product_service.controller.req.ProductCreateReqDto;
import hanghae.product_service.controller.resp.ProductRespDto;
import hanghae.product_service.domain.imagefile.ImageFile;
import hanghae.product_service.domain.product.Product;
import hanghae.product_service.service.ImageFileService;
import hanghae.product_service.service.ProductService;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ProductController {

    private final ProductService productService;
    private final ImageFileService imageFileService;

    public ProductController(ProductService productService, ImageFileService imageFileService) {
        this.productService = productService;
        this.imageFileService = imageFileService;
    }

    @PostMapping("/product/create")
    public ResponseEntity<?> createProduct(@RequestPart ProductCreateReqDto reqDto,
                                           @RequestPart MultipartFile file) throws IOException {
        MultipartUtil.validateImageFile(file);
        FileInfo fileInfo = toFileInfo(file);
        productService.create(reqDto.name(),reqDto.price(), reqDto.quantity(), fileInfo);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<?> getProduct(@PathVariable("id") String id) {
        Product product = productService.getProductByUid(id);
        ImageFile imageFile = imageFileService.getImageFile(product.id());
        return new ResponseEntity<>(ProductRespDto.of(product, imageFile.storeFileName()), HttpStatus.OK);
    }

    private FileInfo toFileInfo(MultipartFile file) throws IOException {
        if(file == null) return null;
        return FileInfo.create(file);
    }
}