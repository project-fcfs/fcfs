package hanghae.product_service.domain.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import hanghae.product_service.service.common.exception.CustomApiException;
import hanghae.product_service.service.common.util.ErrorMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    @DisplayName("재고 수량보다 주문 수량이 많으면 에러를 반환한다")
    void OutOfStockError() throws Exception {
        // given
        String name = "product";
        int quantity = 10;
        int orderCount = 11;
        Product product = createProduct(name, quantity);

        // then
        assertThatThrownBy(() -> product.removeStock(orderCount))
                .isInstanceOf(CustomApiException.class)
                .hasMessage(ErrorMessage.OUT_OF_STOCK.getMessage());
    }

    @Test
    @DisplayName("재고수량이 0이 되면 품절상태가 된다")
    void quantityZero() throws Exception {
        // given
        String name = "product";
        int quantity = 10;
        int orderCount = 10;
        Product product = createProduct(name, quantity);

        // when
        Product result = product.removeStock(orderCount);

        // then
        assertThat(result.status()).isEqualByComparingTo(ProductStatus.SOLD_OUT);
    }

    private Product createProduct(String name, int quantity) {
        return new Product(1L, name, 10, quantity, ProductType.BASIC, ProductStatus.ACTIVE);
    }

}