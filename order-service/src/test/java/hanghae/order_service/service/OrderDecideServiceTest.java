package hanghae.order_service.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import hanghae.order_service.domain.cart.Cart;
import hanghae.order_service.domain.cart.CartProduct;
import hanghae.order_service.domain.order.Delivery;
import hanghae.order_service.domain.order.DeliveryStatus;
import hanghae.order_service.domain.order.Order;
import hanghae.order_service.domain.order.OrderProduct;
import hanghae.order_service.domain.order.OrderStatus;
import hanghae.order_service.mock.FakeCartProductRepository;
import hanghae.order_service.mock.FakeLocalDateTimeHolder;
import hanghae.order_service.mock.FakeOrderProductMessage;
import hanghae.order_service.mock.FakeOrderRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class OrderDecideServiceTest {

    private OrderDecideService orderDecideService;
    private FakeOrderRepository orderRepository;
    private FakeLocalDateTimeHolder localDateTimeHolder;
    private FakeCartProductRepository cartProductRepository;
    private FakeOrderProductMessage orderProductMessage;

    @BeforeEach
    void setUp() {
        orderProductMessage = new FakeOrderProductMessage();
        cartProductRepository = new FakeCartProductRepository();
        localDateTimeHolder = new FakeLocalDateTimeHolder(LocalDateTime.now());
        orderRepository = new FakeOrderRepository();
        orderDecideService = new OrderDecideService(localDateTimeHolder, orderRepository, cartProductRepository,
                orderProductMessage);
    }

    @Nested
    @DisplayName("재고 여부에 따라 주문 성공, 실패가 된다")
    class DecideOrder {

        @Test
        @DisplayName("재고가 부족하면 주문이 취소된다")
        void IfOutOfStockCancel() throws Exception {
            // given
            String orderId = "orderId";
            orderRepository.save(
                    Order.create("user", orderId, null, createDelivery(DeliveryStatus.PREPARING), LocalDateTime.now()));
            int code = -1;
            LocalDateTime localDateTime = LocalDateTime.of(2024, 12, 27, 14, 27);
            localDateTimeHolder.setLocalDateTime(localDateTime);

            // when
            orderDecideService.decideOrder(code, orderId);
            Order result = orderRepository.findById(1L).get();

            // then
            assertAll(() -> {
                assertThat(result.orderStatus()).isEqualByComparingTo(OrderStatus.CANCELLED);
                assertThat(result.delivery().status()).isEqualByComparingTo(DeliveryStatus.CANCELED);
                assertThat(result.updatedAt()).isEqualTo(localDateTime);
                assertThat(result.delivery().updatedAt()).isEqualTo(localDateTime);
            });
        }

        @Test
        @DisplayName("재고가 있으면 주문이 성공하고 장바구니에서 해당 상품들을 삭제한다")
        void CanOrderComplete() throws Exception {
            // given
            String userId = "userId";
            String orderId = "orderId";
            Long productId = 1L;
            int quantity = 10;
            int orderCount = 5;
            saveCartProduct(quantity, productId, userId);
            orderRepository.save(
                    Order.create(userId, orderId, List.of(createOrderProduct(orderCount, productId)),
                            createDelivery(DeliveryStatus.PREPARING), LocalDateTime.now()));
            int code = 1;
            LocalDateTime localDateTime = LocalDateTime.of(2024, 12, 27, 14, 27);
            localDateTimeHolder.setLocalDateTime(localDateTime);

            // when
            orderDecideService.decideOrder(code, orderId);
            Order result = orderRepository.findById(1L).get();

            // then
            Optional<CartProduct> cartProduct = cartProductRepository.findCartProduct(productId, userId);
            assertAll(() -> {
                assertThat(result.orderStatus()).isEqualByComparingTo(OrderStatus.COMPLETED);
                assertThat(result.delivery().status()).isEqualByComparingTo(DeliveryStatus.PREPARING);
                assertThat(result.updatedAt()).isEqualTo(localDateTime);
                assertThat(cartProduct).isEmpty();
            });
        }

    }

    private OrderProduct createOrderProduct(int orderCount, Long productId) {
        return OrderProduct.create(100, orderCount, productId, LocalDateTime.now());
    }

    private void saveCartProduct(int quantity, Long productId, String userId) {
        cartProductRepository.save(new CartProduct(null, quantity, productId, Cart.create(userId)));
    }

    private Delivery createDelivery(DeliveryStatus deliveryStatus) {
        return new Delivery(1L, "address", deliveryStatus, LocalDateTime.now(), LocalDateTime.now());
    }

}