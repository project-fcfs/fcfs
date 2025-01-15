package hanghae.order_service.domain.order;

import static hanghae.order_service.service.common.util.OrderConstant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import hanghae.order_service.service.common.exception.CustomApiException;
import hanghae.order_service.service.common.exception.ErrorCode;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class DeliveryTest {

    @Test
    @DisplayName("배달은 생성시에는 배달 상태는 준비중이다")
    void createDeliveryPreparing() throws Exception {
        // given
        LocalDateTime localDateTime = LocalDateTime.of(2024, 12, 21, 13, 17, 11);
        String address = "주소";

        // when
        Delivery result = Delivery.create(address, localDateTime);

        // then
        assertAll(() -> {
            assertThat(result.status()).isEqualByComparingTo(DeliveryStatus.PREPARING);
            assertThat(result.address()).isEqualTo(address);
        });
    }

    @Test
    @DisplayName("배달 준비중일 때는 취소가 가능하다")
    void canCancelIfPreparing() throws Exception {
        // given
        Delivery delivery = Delivery.create("address", LocalDateTime.now());
        LocalDateTime localDateTime = LocalDateTime.of(2024, 12, 21, 13, 20, 11);

        // when
        Delivery result = delivery.cancelIfPossible(localDateTime);

        // then
        assertAll(() -> {
            assertThat(result.status()).isEqualByComparingTo(DeliveryStatus.CANCELED);
            assertThat(result.updatedAt()).isEqualTo(localDateTime);
        });
    }

    @Nested
    @DisplayName("배달 취소는 배달 상태에 따라 달라진다")
    class cancelDelivery {

        @Test
        @DisplayName("배달 준비 중일 때는 취소가 가능하다")
        void canCancelDelivery() throws Exception {
            // given
            LocalDateTime localDateTime = LocalDateTime.now();
            Delivery delivery = createDelivery(DeliveryStatus.PREPARING, localDateTime);

            // when
            Delivery result = delivery.cancelIfPossible(localDateTime);

            // then
            assertAll(() -> {
                assertThat(result.status()).isEqualByComparingTo(DeliveryStatus.CANCELED);
                assertThat(result.updatedAt()).isEqualTo(localDateTime);
            });
        }

        @Test
        @DisplayName("배달 준비 중이 아닐 때 취소를 하면 예외를 반환한다")
        void ifCancelError() throws Exception {
            // given
            LocalDateTime localDateTime = LocalDateTime.now();
            Delivery delivery = createDelivery(DeliveryStatus.DELIVERING, localDateTime);

            // then
            assertThatThrownBy(() -> delivery.cancelIfPossible(localDateTime))
                    .isInstanceOf(CustomApiException.class)
                    .hasMessage(ErrorCode.ERROR_CANNOT_CANCEL_SHIPPING.getMessage());
        }
    }

    @Nested
    @DisplayName("환불은 배달 상태와 시간에 따라 달라진다")
    class refundDelivery {

        @Test
        @DisplayName("배달 완료와 환불 기한에 맞으면 환불이 가능하다")
        void canCancelDelivery() throws Exception {
            // given
            LocalDateTime localDateTime = LocalDateTime.now();
            Delivery delivery = createDelivery(DeliveryStatus.COMPLETED, localDateTime);

            // when
            Delivery result = delivery.refundIfEligible(localDateTime);

            // then
            assertAll(() -> {
                assertThat(result.status()).isEqualByComparingTo(DeliveryStatus.CANCELED);
                assertThat(result.updatedAt()).isEqualTo(localDateTime);
            });
        }

        @Test
        @DisplayName("환불 기한을 넘으면 환불이 불가능하다")
        void ifOverTheRefundDayCannotRefund() throws Exception {
            // given
            LocalDateTime updatedDate = LocalDateTime.of(2024, 12, 21, 13, 30, 11);
            Delivery delivery = createDelivery(DeliveryStatus.DELIVERING, updatedDate);
            LocalDateTime refundDate = updatedDate.plusDays(RETURN_DAY);

            // then
            assertThatThrownBy(() -> delivery.refundIfEligible(refundDate))
                    .isInstanceOf(CustomApiException.class)
                    .hasMessage(ErrorCode.ERROR_CANNOT_RETURN_SHIPPED.getMessage());
        }
    }

    private Delivery createDelivery(DeliveryStatus deliveryStatus, LocalDateTime currentDate) {
        return new Delivery(1L, "address", deliveryStatus, currentDate, currentDate);
    }

}