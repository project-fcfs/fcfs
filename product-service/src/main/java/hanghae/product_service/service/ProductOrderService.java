package hanghae.product_service.service;

import hanghae.product_service.controller.req.ProductOrderReqDto;
import hanghae.product_service.domain.product.Product;
import hanghae.product_service.service.common.exception.CustomApiException;
import hanghae.product_service.service.port.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class ProductOrderService {

    private static final Logger log = LoggerFactory.getLogger(ProductOrderService.class);
    private final ProductRepository productRepository;

    public ProductOrderService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public void processOrder(List<ProductOrderReqDto> reqDtos) {
        List<String> productIds = reqDtos.stream().map(ProductOrderReqDto::productId).toList();
        List<Product> products = productRepository.findAllByProductIds(productIds);

        Map<String, Product> productsById = products.stream().collect(Collectors.toMap(Product::productId, v -> v));

        List<Product> updatedProducts = new ArrayList<>();
        try {
            for (ProductOrderReqDto reqDto : reqDtos) {
                Product product = productsById.get(reqDto.productId());
                Product updatedProduct = product.removeStock(reqDto.orderCount());
                updatedProducts.add(updatedProduct);
            }

            productRepository.saveAll(updatedProducts);
        } catch (CustomApiException e) {
            log.info("product save error {}", e.getMessage());
        }

        // todo 성공, 취소 카프카로 메시지 보내기

    }
}
