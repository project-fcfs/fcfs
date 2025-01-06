package hanghae.product_service.service.lock;

import hanghae.product_service.controller.req.OrderCreateReqDto;
import hanghae.product_service.controller.req.OrderMessageReqDto;
import hanghae.product_service.domain.product.Product;
import hanghae.product_service.service.port.StockRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PessimisticLockStockService {

    private final StockRepository stockRepository;

    public PessimisticLockStockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Transactional
    public List<Product> processOrder(List<OrderCreateReqDto> reqDtos) {

        List<String> productIds = reqDtos.stream().map(OrderCreateReqDto::productId).toList();
        List<Product> products = stockRepository.findAllByProductIdsWithPessimistic(productIds);

        Map<String, Product> productsById = products.stream().collect(Collectors.toMap(Product::productId, v -> v));

        List<Product> updatedProducts = new ArrayList<>();

        for (OrderCreateReqDto reqDto : reqDtos) {
            Product product = productsById.get(reqDto.productId());
            Product updatedProduct = product.removeStock(reqDto.orderCount());
            updatedProducts.add(updatedProduct);
        }

        return stockRepository.saveAll(updatedProducts);
    }

    @Transactional
    public List<Product> restoreQuantity(List<OrderMessageReqDto> reqDtos) {
        List<String> productIds = reqDtos.stream().map(OrderMessageReqDto::productId).toList();
        List<Product> products = stockRepository.findAllByProductIdsWithPessimistic(productIds);

        Map<String, Product> productsById = products.stream().collect(Collectors.toMap(Product::productId, v -> v));

        List<Product> updatedProducts = new ArrayList<>();

        for (OrderMessageReqDto reqDto : reqDtos) {
            Product product = productsById.get(reqDto.productId());
            Product updatedProduct = product.addStock(reqDto.orderCount());
            updatedProducts.add(updatedProduct);
        }

        return stockRepository.saveAll(updatedProducts);
    }

}
