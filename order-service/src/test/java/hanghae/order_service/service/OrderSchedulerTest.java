package hanghae.order_service.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import hanghae.order_service.domain.order.Delivery;
import hanghae.order_service.domain.order.DeliveryStatus;
import hanghae.order_service.domain.order.Order;
import hanghae.order_service.domain.order.OrderProduct;
import hanghae.order_service.domain.order.OrderStatus;
import hanghae.order_service.domain.product.OrderItem;
import hanghae.order_service.mock.FakeDeliveryRepository;
import hanghae.order_service.mock.FakeLocalDateTimeHolder;
import hanghae.order_service.mock.FakeOrderRepository;
import hanghae.order_service.mock.FakeProductClient;
import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderSchedulerTest {

    private OrderScheduler orderScheduler;
    private FakeLocalDateTimeHolder localDateTimeHolder;
    private FakeDeliveryRepository deliveryRepository;
    private FakeOrderRepository orderRepository;
    private FakeProductClient productClient;

    @BeforeEach
    void setUp() {
        orderRepository = new FakeOrderRepository();
        localDateTimeHolder = new FakeLocalDateTimeHolder(LocalDateTime.now());
        deliveryRepository = new FakeDeliveryRepository();
        productClient = new FakeProductClient(List.of());
        orderScheduler = new OrderScheduler(deliveryRepository, orderRepository, localDateTimeHolder, productClient);
    }

    @Test
    @DisplayName("배달준비중 상태가 하루가 되면 배송중으로 바뀐다")
    void startDeliveryProcess() throws Exception {
        // given
        LocalDateTime localDateTime = LocalDateTime.of(2024, 12, 24, 9, 17, 37);
        saveDelivery(localDateTime, DeliveryStatus.PREPARING);
        LocalDateTime plusDays = localDateTime.plusDays(1);
        localDateTimeHolder.setLocalDateTime(plusDays);

        // when
        orderScheduler.processDelivery();
        Delivery result = deliveryRepository.findById(1L).get();

        // then
        assertAll(() -> {
            assertThat(result.status()).isEqualTo(DeliveryStatus.DELIVERING);
            assertThat(result.updatedAt()).isEqualTo(plusDays);
        });
    }

    @Test
    @DisplayName("배송중 상태가 하루가 지나면 배송완료로 바꾼다")
    void completedDelivery() throws Exception {
        // given
        LocalDateTime localDateTime = LocalDateTime.of(2024, 12, 24, 9, 17, 37);
        saveDelivery(localDateTime, DeliveryStatus.DELIVERING);
        LocalDateTime plusDays = localDateTime.plusDays(1);
        localDateTimeHolder.setLocalDateTime(plusDays);

        // when
        orderScheduler.processCompleteDelivery();
        Delivery result = deliveryRepository.findById(1L).get();

        // then
        assertAll(() -> {
            assertThat(result.status()).isEqualTo(DeliveryStatus.COMPLETED);
            assertThat(result.updatedAt()).isEqualTo(plusDays);
        });
    }

    @Test
    @DisplayName("반품 신청 후 하루가 지나면 반품이 되고 재고에 반영이 된다")
    void canRefundAndStock() throws Exception {
        // given
        String productId = "productId";
        int quantity = 15;
        int orderCount = 12;
        setProductClient(productId, quantity);
        LocalDateTime localDateTime = LocalDateTime.of(2024, 12, 24, 9, 17, 37);
        saveOrder(localDateTime, OrderStatus.RETURN_REQUESTED, orderCount, productId);
        LocalDateTime plusDays = localDateTime.plusDays(1);
        localDateTimeHolder.setLocalDateTime(plusDays);

        // when
        orderScheduler.completeRefund();
        Order result = orderRepository.findById(1L).get();
        List<OrderItem> orderItems = productClient.getOrderItems(List.of(productId));

        // then
        assertAll(() -> {
            assertThat(result.orderStatus()).isEqualByComparingTo(OrderStatus.RETURN_COMPLETED);
            assertThat(result.updatedAt()).isEqualTo(plusDays);
            assertThat(orderItems).hasSize(1)
                    .extracting(OrderItem::orderCount, OrderItem::productId)
                    .containsExactlyInAnyOrder(Tuple.tuple(quantity + orderCount, productId));
        });
    }

    private Delivery saveDelivery(LocalDateTime updatedAt, DeliveryStatus deliveryStatus) {
        Delivery delivery = new Delivery(null, "address", deliveryStatus, LocalDateTime.now(), updatedAt);
        return deliveryRepository.save(delivery);
    }

    private void setProductClient(String productId, int quantity){
        productClient.setOrderItems(List.of(new OrderItem(productId,100, quantity)));
    }

    private void saveOrder(LocalDateTime updatedAt, OrderStatus orderStatus, int orderCount, String productId) {
        OrderProduct orderProduct = createOrderProduct(orderCount, productId);
        Delivery delivery = saveDelivery(LocalDateTime.now(), DeliveryStatus.CANCELED);
        Order order = new Order(null, orderStatus, "userId", "orderId", List.of(orderProduct),
                delivery, LocalDateTime.now(), updatedAt);
        orderRepository.save(order);
    }

    private OrderProduct createOrderProduct(int orderCount, String productId) {
        return new OrderProduct(1L, 1000, orderCount, productId,
                LocalDateTime.now(), null);
    }
}