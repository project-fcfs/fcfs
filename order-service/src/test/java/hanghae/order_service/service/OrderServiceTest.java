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
import hanghae.order_service.domain.product.Product;
import hanghae.order_service.domain.product.Product.ProductStatus;
import hanghae.order_service.mock.FakeCartProductRepository;
import hanghae.order_service.mock.FakeDeliveryRepository;
import hanghae.order_service.mock.FakeLocalDateTimeHolder;
import hanghae.order_service.mock.FakeOrderProducerMessage;
import hanghae.order_service.mock.FakeOrderRepository;
import hanghae.order_service.mock.FakeProduct;
import hanghae.order_service.mock.FakeProductClient;
import hanghae.order_service.mock.FakeUuidRandomHolder;
import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

class OrderServiceTest {

    private OrderService orderService;
    private FakeLocalDateTimeHolder localDateTimeHolder;
    private FakeUuidRandomHolder uuidRandomHolder;
    private FakeProductClient productClient;
    private FakeCartProductRepository cartProductRepository;
    private FakeOrderRepository orderRepository;
    private FakeOrderProducerMessage orderProductMessage;

    @BeforeEach
    void setUp() {
        orderProductMessage = new FakeOrderProducerMessage();
        FakeDeliveryRepository deliveryRepository = new FakeDeliveryRepository();
        productClient = new FakeProductClient();
        orderRepository = new FakeOrderRepository();
        cartProductRepository = new FakeCartProductRepository();
        localDateTimeHolder = new FakeLocalDateTimeHolder(LocalDateTime.now());
        uuidRandomHolder = new FakeUuidRandomHolder("random");
        orderService = new OrderService(orderRepository, cartProductRepository, localDateTimeHolder, uuidRandomHolder,
                productClient, orderProductMessage, deliveryRepository);
    }

    @Test
    @DisplayName("주문을 하면 장바구니에 있는 상품을 가져와서 주문을 한다")
    void createOrder() throws Exception {
        // given
        String productId1 = "product1";
        String productId2 = "product2";
        String userId = "userId";
        int quantity1 = 2;
        int quantity2 = 3;
        List<String> productIds = List.of(productId1, productId2);
        List<CartProduct> cartProducts = List.of(createCartProduct(1L, productId1, quantity1),
                createCartProduct(2L,productId2, quantity2));
        cartProducts.forEach(cartProductRepository::save);
        orderProductMessage.addProduct(FakeProduct.create("name",100,quantity1, productId1));
        orderProductMessage.addProduct(FakeProduct.create("name",100,quantity2, productId2));
        productClient.addData(createProduct(productId1));
        productClient.addData(createProduct(productId2));

        // when
        Order result = orderService.order(productIds, "address", userId);

        // then
        FakeProduct product = orderProductMessage.getProductById(productId1);
        assertAll(() -> {
            assertThat(result.orderStatus()).isEqualByComparingTo(OrderStatus.PENDING);
            assertThat(result.delivery().status()).isEqualByComparingTo(DeliveryStatus.PREPARING);
            assertThat(result.delivery().address()).isEqualTo("address");
            assertThat(result.orderProducts()).hasSize(2)
                    .extracting(OrderProduct::productId, OrderProduct::orderCount)
                    .containsExactlyInAnyOrder(Tuple.tuple(productId1, quantity1), Tuple.tuple(productId2, quantity2));
            assertThat(product.getQuantity()).isEqualTo(0);
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
        orderService.cancel(userId, orderId);
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
        orderService.processRefund(userId, orderId);
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

    @Nested
    @DisplayName("재고 여부에 따라 주문 성공, 실패가 된다")
    class DecideOrder{

        @Test
        @DisplayName("재고가 부족하면 주문이 취소된다")
        void IfOutOfStockCancel() throws Exception {
            // given
            String orderId = "orderId";
            orderRepository.save(Order.create("user", orderId, null, createDelivery(DeliveryStatus.PREPARING), LocalDateTime.now()));
            ResponseEntity<?> response = ResponseEntity.badRequest().build();
            LocalDateTime localDateTime = LocalDateTime.of(2024, 12, 27, 14, 27);
            localDateTimeHolder.setLocalDateTime(localDateTime);

            // when
            orderService.decideOrder(response,orderId);
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
        @DisplayName("재고가 있으면 주문이 성공한다")
        void CanOrderComplete() throws Exception {
            // given
            String orderId = "orderId";
            orderRepository.save(Order.create("user", orderId, null, createDelivery(DeliveryStatus.PREPARING), LocalDateTime.now()));
            ResponseEntity<?> response = ResponseEntity.ok().build();
            LocalDateTime localDateTime = LocalDateTime.of(2024, 12, 27, 14, 27);
            localDateTimeHolder.setLocalDateTime(localDateTime);

            // when
            orderService.decideOrder(response,orderId);
            Order result = orderRepository.findById(1L).get();

            // then
            assertAll(() -> {
                assertThat(result.orderStatus()).isEqualByComparingTo(OrderStatus.COMPLETED);
                assertThat(result.delivery().status()).isEqualByComparingTo(DeliveryStatus.PREPARING);
                assertThat(result.updatedAt()).isEqualTo(localDateTime);
            });
        }

    }

    private Product createProduct(String productId){
        return new Product("name",100,10,productId, ProductStatus.ACTIVE, null);
    }

    private Order createOrder(OrderStatus orderStatus, DeliveryStatus deliveryStatus, String userId, String orderId) {
        return new Order(1L, orderStatus, userId, orderId, null,
                createDelivery(deliveryStatus), LocalDateTime.now(), LocalDateTime.now());
    }

    private Delivery createDelivery(DeliveryStatus deliveryStatus) {
        return new Delivery(1L, "address", deliveryStatus, LocalDateTime.now(), LocalDateTime.now());
    }

    private CartProduct createCartProduct(long id, String productId, int quantity) {
        return new CartProduct(id, quantity, productId, Cart.create("userId"));
    }
}