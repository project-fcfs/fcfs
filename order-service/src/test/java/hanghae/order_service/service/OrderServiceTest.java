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
import hanghae.order_service.domain.product.OrderItem;
import hanghae.order_service.mock.FakeCartProductRepository;
import hanghae.order_service.mock.FakeDeliveryRepository;
import hanghae.order_service.mock.FakeLocalDateTimeHolder;
import hanghae.order_service.mock.FakeOrderRepository;
import hanghae.order_service.mock.FakeProductClient;
import hanghae.order_service.mock.FakeUuidRandomHolder;
import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderServiceTest {

    private OrderService orderService;
    private FakeLocalDateTimeHolder localDateTimeHolder;
    private FakeUuidRandomHolder uuidRandomHolder;
    private FakeProductClient productClient;
    private FakeCartProductRepository cartProductRepository;
    private FakeOrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        FakeDeliveryRepository deliveryRepository = new FakeDeliveryRepository();
        productClient = new FakeProductClient(List.of());
        orderRepository = new FakeOrderRepository();
        cartProductRepository = new FakeCartProductRepository();
        localDateTimeHolder = new FakeLocalDateTimeHolder(LocalDateTime.now());
        uuidRandomHolder = new FakeUuidRandomHolder("random");
        orderService = new OrderService(orderRepository,cartProductRepository,localDateTimeHolder,uuidRandomHolder,productClient,deliveryRepository);

    }

    @Test
    @DisplayName("주문을 하면 장바구니에 있는 상품을 가져와서 주문을 한다")
    void createOrder() throws Exception {
        // given
        String productId1 = "product1";
        String productId2 = "product2";
        String userId = "userId";
        productClient.setOrderItems(List.of(createOrderItem(productId1,3), createOrderItem(productId2,2)));
        List<String> productIds = List.of(productId1, productId2);
        List<CartProduct> cartProducts = List.of(createCartProduct(productId1, 1L, 2),
                createCartProduct(productId2, 2L, 2));
        cartProducts.forEach(cartProductRepository::save);

        // when
        Order result = orderService.order(productIds, "address", userId);

        // then
        assertAll(() -> {
            assertThat(result.orderStatus()).isEqualByComparingTo(OrderStatus.PENDING);
            assertThat(result.delivery().status()).isEqualByComparingTo(DeliveryStatus.PREPARING);
            assertThat(result.delivery().address()).isEqualTo("address");
            assertThat(result.orderProducts()).hasSize(2)
                    .extracting(OrderProduct::productId, OrderProduct::orderCount)
                    .containsExactlyInAnyOrder(Tuple.tuple(productId1, 2), Tuple.tuple(productId2, 2));
        });
    }

    @Test
    @DisplayName("대기 중이거나 배달 준비중이라면 주문 취소가 가능하다")
    void canOrderCancel() throws Exception {
        // given
        String userId = "userId";
        String orderId = "orderId";
        LocalDateTime localDateTime = LocalDateTime.of(2024, 12, 23, 17, 9, 16);
        localDateTimeHolder.setLocalDateTime(localDateTime);
        orderRepository.save(createOrder(OrderStatus.PENDING, DeliveryStatus.PREPARING, userId, orderId));

        // when
        orderService.cancel(userId,orderId);
        Order result = orderRepository.findById(1L).get();

        // then
        assertAll(() -> {
            assertThat(result.orderStatus()).isEqualByComparingTo(OrderStatus.CANCELLED);
            assertThat(result.delivery().status()).isEqualByComparingTo(DeliveryStatus.CANCELED);
            assertThat(result.updatedAt()).isEqualTo(localDateTime);
            assertThat(result.userId()).isEqualTo(userId);
            assertThat(result.orderId()).isEqualTo(orderId);
        });
    }

    @Test
    @DisplayName("환불 가능 기한 내라면 환불 요청이 가능하다")
    void canRequestRefund() throws Exception {
        // given
        String userId = "userId";
        String orderId = "orderId";
        LocalDateTime localDateTime = LocalDateTime.of(2024, 12, 23, 17, 9, 16);
        localDateTimeHolder.setLocalDateTime(localDateTime);
        orderRepository.save(createOrder(OrderStatus.COMPLETED, DeliveryStatus.COMPLETED, userId, orderId));

        // when
        orderService.processRefund(userId,orderId);
        Order result = orderRepository.findById(1L).get();

        // then
        assertAll(() -> {
            assertThat(result.orderStatus()).isEqualByComparingTo(OrderStatus.RETURN_REQUESTED);
            assertThat(result.delivery().status()).isEqualByComparingTo(DeliveryStatus.CANCELED);
            assertThat(result.updatedAt()).isEqualTo(localDateTime);
            assertThat(result.userId()).isEqualTo(userId);
            assertThat(result.orderId()).isEqualTo(orderId);
        });
    }

    private Order createOrder(OrderStatus orderStatus, DeliveryStatus deliveryStatus, String userId, String orderId){
        return new Order(1L, orderStatus, userId, orderId, null,
                createDelivery(deliveryStatus), LocalDateTime.now(), LocalDateTime.now());
    }

    private Delivery createDelivery(DeliveryStatus deliveryStatus){
        return new Delivery(1L, "address", deliveryStatus, LocalDateTime.now(), LocalDateTime.now());
    }

    private OrderItem createOrderItem(String productId, int orderCount) {
        return new OrderItem(productId, 1000, orderCount);
    }

    private CartProduct createCartProduct(String productId, long id, int quantity) {
        return new CartProduct(id, quantity, productId, Cart.create("userId"));
    }
}