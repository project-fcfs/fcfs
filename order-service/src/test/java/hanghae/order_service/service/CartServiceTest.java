package hanghae.order_service.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import hanghae.order_service.domain.cart.CartProduct;
import hanghae.order_service.mock.FakeProductClient;
import hanghae.order_service.mock.FakeCartProductRepository;
import hanghae.order_service.mock.FakeCartRepository;
import hanghae.order_service.service.common.exception.CustomApiException;
import hanghae.order_service.service.common.util.ErrorMessage;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class CartServiceTest {

    private CartService cartService;
    private FakeCartProductRepository cartProductRepository;

    @BeforeEach
    void setUp() {
        FakeProductClient cartProductClient = new FakeProductClient();
        cartProductRepository = new FakeCartProductRepository();
        FakeCartRepository cartRepository = new FakeCartRepository();
        cartService = new CartService(cartRepository, cartProductRepository, cartProductClient);
    }

    @Test
    @DisplayName("상품을 장바구니에 저장할 수 있다")
    void canAddShop() throws Exception {
        // given
        String productId = "product";
        String userId = "user";

        // when
        cartService.add(userId, productId);
        CartProduct result = cartProductRepository.findCartProduct(productId, userId).get();

        // then
        assertAll(() -> {
            assertThat(result.productId()).isEqualTo(productId);
            assertThat(result.cart().userId()).isEqualTo(userId);
            assertThat(result.quantity()).isEqualTo(1);
        });
    }

    @Nested
    @DisplayName("장바구니에 있는 상품의 수량을 업데이트할 수 있다")
    class cartQuantityUpdate{

        @Test
        @DisplayName("올바른 값을 입력하면 장바구니 수량을 업데이트 할 수 있다")
        void canUpdateCartProduct() throws Exception {
            // given
            String productId = "product";
            String userId = "user";
            int count = 5;
            cartService.add(userId, productId);

            // when
            cartService.updateQuantity(userId,productId,count);
            CartProduct result = cartProductRepository.findCartProduct(productId, userId).get();

            // then
            assertAll(() -> {
                assertThat(result.productId()).isEqualTo(productId);
                assertThat(result.cart().userId()).isEqualTo(userId);
                assertThat(result.quantity()).isEqualTo(count + 1);
            });
        }

        @Test
        @DisplayName("장바구니 수량보다 많이 양을 빼려고 하면 예외를 반환한다")
        void outOfStockCartProduct() throws Exception {
            // given
            String productId = "product";
            String userId = "user";
            int count = -5;
            cartService.add(userId, productId);

            // then
            assertThatThrownBy(() -> cartService.updateQuantity(userId, productId, count))
                    .isInstanceOf(CustomApiException.class)
                    .hasMessage(ErrorMessage.OUT_OF_STOCK_CART.getMessage());
        }
    }

    @Nested
    @DisplayName("장바구니에 있는 상품의 삭제할 수 있다")
    class deleteCartProduct{

        @Test
        @DisplayName("장바구니에 있는 상품을 지울 수 있다")
        void canUpdateCartProduct() throws Exception {
            // given
            String productId = "product";
            String userId = "user";
            cartService.add(userId, productId);

            // when
            cartService.deleteProduct(userId,productId);
            Optional<CartProduct> result = cartProductRepository.findCartProduct(productId, userId);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("장바구니에 없는 상품을 지우려고 하면 예외를 반환한다")
        void outOfStockCartProduct() throws Exception {
            // given
            String productId = "product";
            String userId = "user";

            // then
            assertThatThrownBy(() -> cartService.deleteProduct(userId, productId))
                    .isInstanceOf(CustomApiException.class)
                    .hasMessage(ErrorMessage.NOT_FOUND_CART_PRODUCT.getMessage());
        }
    }

}