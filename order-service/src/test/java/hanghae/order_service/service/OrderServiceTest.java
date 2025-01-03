package hanghae.order_service.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
import hanghae.order_service.mock.FakeLocalDateTimeHolder;
import hanghae.order_service.mock.FakeOrderProductMessage;
import hanghae.order_service.mock.FakeOrderRepository;
import hanghae.order_service.mock.FakeProduct;
import hanghae.order_service.mock.FakeProductClient;
import hanghae.order_service.mock.FakeUuidRandomHolder;
import hanghae.order_service.service.common.exception.CustomApiException;
import hanghae.order_service.service.common.util.ErrorMessage;
import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class OrderServiceTest {

    private OrderService orderService;
    private FakeLocalDateTimeHolder localDateTimeHolder;
    private FakeUuidRandomHolder uuidRandomHolder;
    private FakeProductClient productClient;
    private FakeCartProductRepository cartProductRepository;
    private FakeOrderRepository orderRepository;
    private FakeOrderProductMessage orderProductMessage;

    @BeforeEach
    void setUp() {
        orderProductMessage = new FakeOrderProductMessage();
        productClient = new FakeProductClient();
        orderRepository = new FakeOrderRepository();
        cartProductRepository = new FakeCartProductRepository();
        localDateTimeHolder = new FakeLocalDateTimeHolder(LocalDateTime.now());
        uuidRandomHolder = new FakeUuidRandomHolder("random");
        orderService = new OrderService(orderRepository, cartProductRepository, localDateTimeHolder, uuidRandomHolder,
                productClient, orderProductMessage);
    }

    @Nested
    @DisplayName("일반 주문 시에 상품의 재고에 따라 주문 성공, 실패가 된다")
    class processOrder{
        @Test
        @DisplayName("주문을 하면 장바구니에 있는 상품을 가져와서 주문을 한다")
        void createOrder() throws Exception {
            // given
            String productId1 = "product1";
            String productId2 = "product2";
            String userId = "userId";
            int orderCount1 = 2;
            int orderCount2 = 3;
            int quantity = 10;
            List<String> productIds = List.of(productId1, productId2);
            List<CartProduct> cartProducts = List.of(createCartProduct(1L, productId1, orderCount1),
                    createCartProduct(2L, productId2, orderCount2));
            cartProducts.forEach(cartProductRepository::save);
            orderProductMessage.addProduct(FakeProduct.create("name", 100, orderCount1, productId1));
            orderProductMessage.addProduct(FakeProduct.create("name", 100, orderCount2, productId2));
            productClient.addData(createProduct(productId1, quantity));
            productClient.addData(createProduct(productId2, quantity));

            // when
            Order result = orderService.order(productIds, "address", userId);

            // then
            assertAll(() -> {
                assertThat(result.orderStatus()).isEqualByComparingTo(OrderStatus.PENDING);
                assertThat(result.delivery().status()).isEqualByComparingTo(DeliveryStatus.PREPARING);
                assertThat(result.delivery().address()).isEqualTo("address");
                assertThat(result.orderProducts()).hasSize(2)
                        .extracting(OrderProduct::productId, OrderProduct::orderCount)
                        .containsExactlyInAnyOrder(Tuple.tuple(productId1, orderCount1), Tuple.tuple(productId2, orderCount2));
            });
        }

        @Test
        @DisplayName("주문을 했는데 재고가 없으면 예외를 반환한다")
        void processOrderOutOfStock() throws Exception {
            // given
            String productId1 = "product1";
            String productId2 = "product2";
            String userId = "userId";
            int orderCount1 = 2;
            int orderCount2 = 3;
            int quantity = 1;
            List<String> productIds = List.of(productId1, productId2);
            List<CartProduct> cartProducts = List.of(createCartProduct(1L, productId1, orderCount1),
                    createCartProduct(2L, productId2, orderCount2));
            cartProducts.forEach(cartProductRepository::save);
            orderProductMessage.addProduct(FakeProduct.create("name", 100, orderCount1, productId1));
            orderProductMessage.addProduct(FakeProduct.create("name", 100, orderCount2, productId2));
            productClient.addData(createProduct(productId1, quantity));
            productClient.addData(createProduct(productId2, quantity));

            // then
            assertThatThrownBy(() -> orderService.order(productIds, "address", userId))
                    .isInstanceOf(CustomApiException.class)
                    .hasMessage(ErrorMessage.OUT_OF_STOCK.getMessage());
        }
    }

    @Nested
    @DisplayName("선착순 구매는 주문시간 여부에 따라 주문 성공, 실패가 된다")
    class processFcfs{

        @Test
        @DisplayName("영업시간 내에 주문을 하게 되면 주문이 가능하다")
        void processFcfsOrder() throws Exception {
            // given
            LocalDateTime localDateTime = LocalDateTime.of(2025, 1, 2, 9, 0, 0);
            localDateTimeHolder.setLocalDateTime(localDateTime);
            String productId1 = "product1";
            String userId = "userId";
            int orderCount1 = 2;
            int quantity = 10;
            orderProductMessage.addProduct(FakeProduct.create("name", 100, orderCount1, productId1));
            productClient.addData(createProduct(productId1, quantity));

            // when
            Order result = orderService.fcfsOrder(productId1, orderCount1,"address", userId);

            // then
            assertAll(() -> {
                assertThat(result.orderStatus()).isEqualByComparingTo(OrderStatus.PENDING);
                assertThat(result.delivery().status()).isEqualByComparingTo(DeliveryStatus.PREPARING);
                assertThat(result.delivery().address()).isEqualTo("address");
                assertThat(result.orderProducts()).hasSize(1)
                        .extracting(OrderProduct::productId, OrderProduct::orderCount)
                        .containsExactlyInAnyOrder(Tuple.tuple(productId1, orderCount1));
            });
        }

        @Test
        @DisplayName("영업시간 전에 주문을 하게 되면 예외를 반환한다")
        void notOpenTimeError() throws Exception {
            // given
            LocalDateTime localDateTime = LocalDateTime.of(2025, 1, 2, 8, 59, 59);
            localDateTimeHolder.setLocalDateTime(localDateTime);
            String productId1 = "product1";
            String userId = "userId";
            int orderCount1 = 2;
            int quantity = 10;
            List<CartProduct> cartProducts = List.of(createCartProduct(1L, productId1, orderCount1));
            cartProducts.forEach(cartProductRepository::save);
            orderProductMessage.addProduct(FakeProduct.create("name", 100, orderCount1, productId1));
            productClient.addData(createProduct(productId1, quantity));

            // when
            assertThatThrownBy(() -> orderService.fcfsOrder(productId1, orderCount1, "addres", userId))
                    .isInstanceOf(CustomApiException.class)
                    .hasMessage(ErrorMessage.NOT_OPEN_TIME.getMessage());
        }
    }


    @Test
    @DisplayName("장바구니 속 상품이 없다면 예외를 반환한다")
    void emptyCartProductError() throws Exception {
        // given
        String productId = "product1";

        // then
        assertThatThrownBy(() -> orderService.order(List.of(productId), "address", "userId"))
                .isInstanceOf(CustomApiException.class)
                .hasMessage(ErrorMessage.NOT_FOUND_CART_PRODUCT.getMessage());
    }

    @Test
    @DisplayName("대기 중이거나 배달 준비중이라면 주문 취소가 가능하다")
    void canOrderCancel() throws Exception {
        // given
        String userId = "userId";
        String orderId = "orderId";
        String productId = "productId";
        int quantity = 10;
        int orderCount = 5;
        OrderProduct orderProduct = createOrderProduct(orderCount, productId);
        orderProductMessage.addProduct(FakeProduct.create("name", 100, quantity, productId));

        LocalDateTime localDateTime = LocalDateTime.of(2024, 12, 23, 17, 9, 16);
        localDateTimeHolder.setLocalDateTime(localDateTime);
        orderRepository.save(
                createOrder(OrderStatus.PENDING, DeliveryStatus.PREPARING, userId, orderId, List.of(orderProduct)));

        // when
        orderService.cancel(userId, orderId);
        Order result = orderRepository.findById(1L).get();

        // then
        FakeProduct product = orderProductMessage.getProductById(productId);
        assertAll(() -> {
            assertThat(result.orderStatus()).isEqualByComparingTo(OrderStatus.CANCELLED);
            assertThat(result.delivery().status()).isEqualByComparingTo(DeliveryStatus.CANCELED);
            assertThat(result.updatedAt()).isEqualTo(localDateTime);
            assertThat(result.userId()).isEqualTo(userId);
            assertThat(result.orderId()).isEqualTo(orderId);
            assertThat(product.getQuantity()).isEqualTo(quantity + orderCount);
        });
    }

    @Test
    @DisplayName("환불 신청할 때 주문한 내역이 없다면 예외를 반환한다")
    void notOrderError() throws Exception {
        // given
        String productId = "product1";
        String orderId = "orderId";

        // then
        assertThatThrownBy(() -> orderService.processRefund(orderId, productId))
                .isInstanceOf(CustomApiException.class)
                .hasMessage(ErrorMessage.NOT_FOUND_ORDER.getMessage());
    }

    @Test
    @DisplayName("환불 가능 기한 내라면 환불 요청이 가능하다")
    void canRequestRefund() throws Exception {
        // given
        String userId = "userId";
        String orderId = "orderId";
        LocalDateTime localDateTime = LocalDateTime.of(2024, 12, 23, 17, 9, 16);
        localDateTimeHolder.setLocalDateTime(localDateTime);
        orderRepository.save(createOrder(OrderStatus.COMPLETED, DeliveryStatus.COMPLETED, userId, orderId, null));

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

    private Product createProduct(String productId, int quantity) {
        return new Product("name", 100, quantity, productId, ProductStatus.ACTIVE, null);
    }

    private Order createOrder(OrderStatus orderStatus, DeliveryStatus deliveryStatus, String userId, String orderId,
                              List<OrderProduct> orderProducts) {
        return new Order(1L, orderStatus, userId, orderId, orderProducts,
                createDelivery(deliveryStatus), LocalDateTime.now(), LocalDateTime.now());
    }

    private Delivery createDelivery(DeliveryStatus deliveryStatus) {
        return new Delivery(1L, "address", deliveryStatus, LocalDateTime.now(), LocalDateTime.now());
    }

    private CartProduct createCartProduct(long id, String productId, int quantity) {
        return new CartProduct(id, quantity, productId, Cart.create("userId"));
    }

    private OrderProduct createOrderProduct(int orderCount, String productId) {
        return OrderProduct.create(100, orderCount, productId, LocalDateTime.now());
    }
}