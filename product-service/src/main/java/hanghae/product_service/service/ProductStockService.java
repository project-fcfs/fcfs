package hanghae.product_service.service;

import hanghae.product_service.controller.req.StockUpdateReqDto;
import hanghae.product_service.domain.product.Product;
import hanghae.product_service.service.port.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ProductStockService {

    private final ProductRepository productRepository;

    public ProductStockService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Product> processOrder(List<StockUpdateReqDto> reqDtos) {
        Map<Long, Product> productsById = getProductsByIdMap(reqDtos);

        List<Product> updatedProducts = new ArrayList<>();

        for (StockUpdateReqDto reqDto : reqDtos) {
            Product product = productsById.get(reqDto.productId());
            Product updatedProduct = product.removeStock(reqDto.orderCount());
            updatedProducts.add(updatedProduct);
        }

        return productRepository.saveAll(updatedProducts);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Product> restoreQuantity(List<StockUpdateReqDto> reqDtos) {
        Map<Long, Product> productsById = getProductsByIdMap(reqDtos);

        List<Product> updatedProducts = new ArrayList<>();

        for (StockUpdateReqDto reqDto : reqDtos) {
            Product product = productsById.get(reqDto.productId());
            Product updatedProduct = product.addStock(reqDto.orderCount());
            updatedProducts.add(updatedProduct);
        }

        return productRepository.saveAll(updatedProducts);
    }

    private Map<Long, Product> getProductsByIdMap(List<StockUpdateReqDto> reqDtos) {
        List<Long> productIds = reqDtos.stream().map(StockUpdateReqDto::productId).toList();
        List<Product> products = productRepository.findAllByProductIds(productIds);

        return products.stream().collect(Collectors.toMap(Product::id, v -> v));
    }

}
