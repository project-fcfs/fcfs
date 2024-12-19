package hanghae.product_service.service.product;

import hanghae.product_service.controller.req.FileInfo;
import hanghae.product_service.controller.req.ProductCreateReqDto;
import hanghae.product_service.domain.imagefile.ImageFileCreateEvent;
import hanghae.product_service.domain.product.Product;
import hanghae.product_service.service.port.ProductRepository;
import hanghae.product_service.service.port.UUIDRandomHolder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final ApplicationEventPublisher publisher;
    private final UUIDRandomHolder uuidRandomHolder;

    public ProductService(ProductRepository productRepository, ApplicationEventPublisher publisher,
                          UUIDRandomHolder uuidRandomHolder) {
        this.productRepository = productRepository;
        this.publisher = publisher;
        this.uuidRandomHolder = uuidRandomHolder;
    }

    @Transactional
    public void create(ProductCreateReqDto reqDto, FileInfo fileInfo) {
        Product product = Product.create(reqDto.name(), reqDto.price(),
                reqDto.quantity(), uuidRandomHolder.getRandomUUID());

        Product savedProduct = productRepository.save(product);

        publisher.publishEvent(new ImageFileCreateEvent(fileInfo, savedProduct));
    }
}
