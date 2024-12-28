package hanghae.product_service.service;

import hanghae.product_service.controller.req.OrderMessageReqDto;
import hanghae.product_service.domain.product.Product;
import hanghae.product_service.service.common.exception.CustomApiException;
import hanghae.product_service.service.port.OrderProductMessage;
import hanghae.product_service.service.port.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ProductStockService {

    private static final Logger log = LoggerFactory.getLogger(ProductStockService.class);
    private final ProductRepository productRepository;
    private final OrderProductMessage orderProductMessage;

    public ProductStockService(ProductRepository productRepository, OrderProductMessage orderProductMessage) {
        this.productRepository = productRepository;
        this.orderProductMessage = orderProductMessage;
    }

    @Transactional
    public void processOrder(List<OrderMessageReqDto> reqDtos) {

        List<String> productIds = reqDtos.stream().map(OrderMessageReqDto::productId).toList();
        List<Product> products = productRepository.findAllByProductIds(productIds);

        Map<String, Product> productsById = products.stream().collect(Collectors.toMap(Product::productId, v -> v));

        List<Product> updatedProducts = new ArrayList<>();
        try {
            for (OrderMessageReqDto reqDto : reqDtos) {
                Product product = productsById.get(reqDto.productId());
                Product updatedProduct = product.removeStock(reqDto.orderCount());
                updatedProducts.add(updatedProduct);
            }

            productRepository.saveAll(updatedProducts);
            orderProductMessage.sendResult(1, "success", reqDtos.getFirst().orderId());
        } catch (CustomApiException e) {
            log.info("product save error {}", e.getMessage());
            orderProductMessage.sendResult(-1, e.getMessage(), reqDtos.getFirst().orderId());
        }

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
