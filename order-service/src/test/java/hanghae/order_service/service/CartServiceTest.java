package hanghae.order_service.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import hanghae.order_service.domain.cart.Cart;
import hanghae.order_service.domain.cart.CartProduct;
import hanghae.order_service.domain.product.Product;
import hanghae.order_service.mock.FakeCartProductRepository;
import hanghae.order_service.mock.FakeCartRepository;
import hanghae.order_service.mock.FakeProductClient;
import hanghae.order_service.service.common.exception.CustomApiException;
import hanghae.order_service.service.common.exception.ErrorCode;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class CartServiceTest {

    private CartService cartService;
    private FakeCartProductRepository cartProductRepository;
    private FakeProductClient cartProductClient;

    @BeforeEach
    void setUp() {
        cartProductClient = new FakeProductClient();
        cartProductRepository = new FakeCartProductRepository();
        FakeCartRepository cartRepository = new FakeCartRepository();
        cartService = new CartService(cartRepository, cartProductRepository, cartProductClient);
    }

    @Test
    @DisplayName("상품을 장바구니에 저장할 수 있다")
    void canAddShop() throws Exception {
        // given
        Long productId = 1L;
        String userId = "user";
        int count = 1;
        cartProductClient.addData(createProduct(productId));

        // when
        cartService.add(userId, productId, count);
        CartProduct result = cartProductRepository.findCartProduct(productId, userId).get();

        // then
        assertAll(() -> {
            assertThat(result.productId()).isEqualTo(productId);
            assertThat(result.cart().userId()).isEqualTo(userId);
            assertThat(result.quantity()).isEqualTo(1);
        });
    }

    @Test
    @DisplayName("유효하지 않은 상품을 장바구니에 담으려고 할 경우 예외가 발생한다")
    void putInvalidProduct() throws Exception {
        // then
        assertThatThrownBy(() -> cartService.add("userId", 3L, 3))
                .isInstanceOf(CustomApiException.class)
                .hasMessage(ErrorCode.INVALID_PRODUCT.getMessage());
    }

    @Nested
    @DisplayName("장바구니에 있는 상품의 수량을 업데이트할 수 있다")
    class cartQuantityUpdate {

        @Test
        @DisplayName("올바른 값을 입력하면 장바구니 수량을 업데이트 할 수 있다")
        void canUpdateCartProduct() throws Exception {
            // given
            Long productId = 1L;
            String userId = "user";
            int count = 5;
            cartProductRepository.save(CartProduct.create(productId, count, Cart.create(userId)));

            // when
            cartService.updateQuantity(userId, productId, count);
            CartProduct result = cartProductRepository.findCartProduct(productId, userId).get();

            // then
            assertAll(() -> {
                assertThat(result.productId()).isEqualTo(productId);
                assertThat(result.cart().userId()).isEqualTo(userId);
                assertThat(result.quantity()).isEqualTo(count * 2);
            });
        }

        @Test
        @DisplayName("장바구니 수량보다 많이 양을 빼려고 하면 예외를 반환한다")
        void outOfStockCartProduct() throws Exception {
            // given
            Long productId = 1L;
            String userId = "user";
            int count = 1;
            int updateCount = -5;
            cartProductRepository.save(CartProduct.create(productId, count, Cart.create(userId)));

            // then
            assertThatThrownBy(() -> cartService.updateQuantity(userId, productId, updateCount))
                    .isInstanceOf(CustomApiException.class)
                    .hasMessage(ErrorCode.OUT_OF_STOCK_CART.getMessage());
        }
    }

    @Nested
    @DisplayName("장바구니에 있는 상품의 삭제할 수 있다")
    class deleteCartProduct {

        @Test
        @DisplayName("장바구니에 있는 상품을 지울 수 있다")
        void canUpdateCartProduct() throws Exception {
            // given
            Long productId = 1L;
            String userId = "user";
            int count = 1;
            cartProductRepository.save(CartProduct.create(productId, count, Cart.create(userId)));

            // when
            cartService.deleteProduct(userId, List.of(productId));
            Optional<CartProduct> result = cartProductRepository.findCartProduct(productId, userId);

            // then
            assertThat(result).isEmpty();
        }
    }

    private Product createProduct(Long productId) {
        return new Product(productId, "name", 100, 10);
    }

}