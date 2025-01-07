package hanghae.product_service.service.lock;

import hanghae.product_service.controller.req.StockUpdateReqDto;
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
    public List<Product> processOrder(List<StockUpdateReqDto> reqDtos) {
        Map<Long, Product> productsById = getProductsByIdMap(reqDtos);

        List<Product> updatedProducts = new ArrayList<>();

        for (StockUpdateReqDto reqDto : reqDtos) {
            Product product = productsById.get(reqDto.productId());
            Product updatedProduct = product.removeStock(reqDto.orderCount());
            updatedProducts.add(updatedProduct);
        }

        return stockRepository.saveAll(updatedProducts);
    }

    @Transactional
    public List<Product> restoreQuantity(List<StockUpdateReqDto> reqDtos) {
        Map<Long, Product> productsById = getProductsByIdMap(reqDtos);

        List<Product> updatedProducts = new ArrayList<>();

        for (StockUpdateReqDto reqDto : reqDtos) {
            Product product = productsById.get(reqDto.productId());
            Product updatedProduct = product.addStock(reqDto.orderCount());
            updatedProducts.add(updatedProduct);
        }

        return stockRepository.saveAll(updatedProducts);
    }

    private Map<Long, Product> getProductsByIdMap(List<StockUpdateReqDto> reqDtos) {
        List<Long> productIds = reqDtos.stream().map(StockUpdateReqDto::productId).toList();
        List<Product> products = stockRepository.findAllByProductIdsWithPessimistic(productIds);

        return products.stream().collect(Collectors.toMap(Product::id, v -> v));
    }

}
