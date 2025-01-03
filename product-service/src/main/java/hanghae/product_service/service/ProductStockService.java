package hanghae.product_service.service;

import hanghae.product_service.controller.req.OrderCreateReqDto;
import hanghae.product_service.controller.req.OrderMessageReqDto;
import hanghae.product_service.domain.product.Product;
import hanghae.product_service.service.port.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ProductStockService {

    private static final Logger log = LoggerFactory.getLogger(ProductStockService.class);
    private final ProductRepository productRepository;

    public ProductStockService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Product> processOrder(List<OrderCreateReqDto> reqDtos) {

        List<String> productIds = reqDtos.stream().map(OrderCreateReqDto::productId).toList();
        List<Product> products = productRepository.findAllByProductIds(productIds);

        Map<String, Product> productsById = products.stream().collect(Collectors.toMap(Product::productId, v -> v));

        List<Product> updatedProducts = new ArrayList<>();

        for (OrderCreateReqDto reqDto : reqDtos) {
            Product product = productsById.get(reqDto.productId());
            Product updatedProduct = product.removeStock(reqDto.orderCount());
            updatedProducts.add(updatedProduct);
        }

        return productRepository.saveAll(updatedProducts);
    }

    @Transactional
    public void restoreQuantity(List<OrderMessageReqDto> reqDtos) {
        List<String> productIds = reqDtos.stream().map(OrderMessageReqDto::productId).toList();
        List<Product> products = productRepository.findAllByProductIds(productIds);

        Map<String, Product> productsById = products.stream().collect(Collectors.toMap(Product::productId, v -> v));

        List<Product> updatedProducts = new ArrayList<>();

        for (OrderMessageReqDto reqDto : reqDtos) {
            Product product = productsById.get(reqDto.productId());
            Product updatedProduct = product.addStock(reqDto.orderCount());
            updatedProducts.add(updatedProduct);
        }

        productRepository.saveAll(updatedProducts);
    }

}
