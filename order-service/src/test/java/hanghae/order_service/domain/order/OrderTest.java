package hanghae.order_service.domain.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import hanghae.order_service.service.common.exception.CustomApiException;
import hanghae.order_service.service.common.util.ErrorMessage;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    @DisplayName("주문을 취소할 수 있다")
    void canCancelOrder() throws Exception {
        // given
        LocalDateTime createdDate = LocalDateTime.now();
        Order order = createOrder(createdDate, OrderStatus.PENDING, DeliveryStatus.PREPARING);
        LocalDateTime currentDate = LocalDateTime.of(2024, 12, 21, 13, 41);

        // when
        Order result = order.cancelOrder(currentDate);

        // then
        assertAll(() -> {
            assertThat(result.orderStatus()).isEqualTo(OrderStatus.CANCELLED);
            assertThat(result.updatedAt()).isEqualTo(currentDate);
        });
    }

    @Test
    @DisplayName("환불기한 내라면 환불신청이 가능하다")
    void canRefundOrder() throws Exception {
        // given
        LocalDateTime createdDate = LocalDateTime.of(2024, 12, 21, 12, 41);
        Order order = createOrder(createdDate, OrderStatus.COMPLETED, DeliveryStatus.COMPLETED);
        LocalDateTime currentDate = LocalDateTime.of(2024, 12, 21, 13, 41);

        // when
        Order result = order.requestRefund(currentDate);

        // then
        assertAll(() -> {
            assertThat(result.orderStatus()).isEqualTo(OrderStatus.RETURN_REQUESTED);
            assertThat(result.updatedAt()).isEqualTo(currentDate);
        });
    }

    @Test
    @DisplayName("환불요청을 보내고 일정 기한 뒤에 환불완료가 된다")
    void completedRefund() throws Exception {
        // given
        LocalDateTime createdDate = LocalDateTime.of(2024, 12, 21, 12, 41);
        Order order = createOrder(createdDate, OrderStatus.RETURN_REQUESTED, DeliveryStatus.COMPLETED);
        LocalDateTime currentDate = LocalDateTime.of(2024, 12, 21, 13, 41);

        // when
        Order result = order.completedRefund(currentDate);

        // then
        assertAll(() -> {
            assertThat(result.orderStatus()).isEqualTo(OrderStatus.RETURN_COMPLETED);
            assertThat(result.updatedAt()).isEqualTo(currentDate);
        });
    }

    @Test
    @DisplayName("주문완료 상태가 아니라면 에러를 반환한다")
    void NotCompletedError() throws Exception {
        // given
        LocalDateTime createdDate = LocalDateTime.of(2024, 12, 21, 12, 41);
        Order order = createOrder(createdDate, OrderStatus.PENDING, DeliveryStatus.COMPLETED);
        LocalDateTime currentDate = LocalDateTime.of(2024, 12, 21, 13, 41);

        // then
        assertThatThrownBy(() -> order.requestRefund(currentDate))
                .isInstanceOf(CustomApiException.class)
                .hasMessage(ErrorMessage.ERROR_REQUEST_REFUND.getMessage());
    }

    @Test
    @DisplayName("환불 요청상태가 아니라면 에러를 반환한다")
    void NotRefundDateError() throws Exception {
        // given
        LocalDateTime createdDate = LocalDateTime.of(2024, 12, 21, 12, 41);
        Order order = createOrder(createdDate, OrderStatus.PENDING, DeliveryStatus.COMPLETED);
        LocalDateTime currentDate = LocalDateTime.of(2024, 12, 21, 13, 41);

        // then
        assertThatThrownBy(() -> order.completedRefund(currentDate))
                .isInstanceOf(CustomApiException.class)
                .hasMessage(ErrorMessage.ERROR_COMPLETE_REFUND.getMessage());
    }

    @Test
    @DisplayName("재고가 없으면 주문을 취소 상태가 된다")
    void ifOutOfStockCancel() throws Exception {
        // given
        LocalDateTime createdDate = LocalDateTime.of(2024, 12, 21, 12, 41);
        Order order = createOrder(createdDate, OrderStatus.PENDING, DeliveryStatus.PREPARING);
        LocalDateTime currentDate = LocalDateTime.of(2024, 12, 21, 13, 41);

        // when
        Order result = order.handlerOutOfStock(currentDate);

        // then
        assertAll(() -> {
            assertThat(result.orderStatus()).isEqualTo(OrderStatus.CANCELLED);
            assertThat(result.updatedAt()).isEqualTo(currentDate);
        });
    }

    private Order createOrder(LocalDateTime currentDate, OrderStatus orderStatus, DeliveryStatus deliveryStatus){
        Delivery delivery = createDelivery(deliveryStatus, currentDate);
        return new Order(1L, orderStatus, "user", "order", null,
                delivery, currentDate, currentDate);
    }

    private Delivery createDelivery(DeliveryStatus deliveryStatus, LocalDateTime currentDate) {
        return new Delivery(1L, "address", deliveryStatus, currentDate, currentDate);
    }

}