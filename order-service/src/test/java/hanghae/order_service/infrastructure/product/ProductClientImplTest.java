package hanghae.order_service.infrastructure.product;

import hanghae.order_service.IntegrationTestSupport;
import hanghae.order_service.domain.product.Product;
import hanghae.order_service.service.port.ProductClient;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;


class ProductClientImplTest extends IntegrationTestSupport {

    @Autowired
    private ProductClient productClient;

    @Test
    @DisplayName("")
    void 이름_test() throws Exception {
        // given
        ResponseEntity<?> validProduct = productClient.isValidProduct("123");
        System.out.println("validProduct.getStatusCode() = " + validProduct.getStatusCode());

        // when

        // then
    }

    @Test
    @DisplayName("")
    void 이름_test2() throws Exception {
        // given
        ResponseEntity<List<Product>> products = productClient.getProducts(List.of("test"));
        System.out.println("products.getStatusCode() = " + products.getStatusCode());
        System.out.println("products.getBody() = " + products.getBody());

        // when

        // then
    }

}