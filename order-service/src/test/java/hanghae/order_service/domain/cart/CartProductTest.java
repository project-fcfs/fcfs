package hanghae.order_service.domain.cart;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import hanghae.order_service.service.common.exception.CustomApiException;
import hanghae.order_service.service.common.util.ErrorMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CartProductTest {

    @Test
    @DisplayName("장바구니 속 상품의 수량을 0이하로 줄이려고 하면 예외를 반환한다")
    void cartProductQuantityUnder0Error() throws Exception {
        // given
        int quantity = 10;
        CartProduct cartProduct = new CartProduct(1L, quantity, "productId", Cart.create("userId"));
        int orderCount = -10;

        // then
        assertThatThrownBy(() -> cartProduct.updateCount(orderCount))
                .isInstanceOf(CustomApiException.class)
                .hasMessage(ErrorMessage.OUT_OF_STOCK_CART.getMessage());
    }

}