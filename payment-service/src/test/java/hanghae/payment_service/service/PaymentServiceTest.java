package hanghae.payment_service.service;

import static hanghae.payment_service.service.common.util.PaymentConst.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import hanghae.payment_service.domain.Payment;
import hanghae.payment_service.mock.FakeLocalDateTimeHolder;
import hanghae.payment_service.mock.FakeOrderMessage;
import hanghae.payment_service.mock.FakePaymentRepository;
import hanghae.payment_service.mock.FakeRandomHolder;
import hanghae.payment_service.mock.FakeUuidRandomHolder;
import hanghae.payment_service.service.common.util.PaymentConst;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PaymentServiceTest {

    private PaymentService paymentService;
    private FakePaymentRepository paymentRepository;
    private FakeUuidRandomHolder uuidRandomHolder;
    private FakeLocalDateTimeHolder localDateTimeHolder;
    private FakeOrderMessage orderMessage;
    private FakeRandomHolder randomHolder;

    @BeforeEach
    void setUp() {
        orderMessage = new FakeOrderMessage();
        localDateTimeHolder = new FakeLocalDateTimeHolder(LocalDateTime.now());
        uuidRandomHolder = new FakeUuidRandomHolder("random");
        paymentRepository = new FakePaymentRepository();
        randomHolder = new FakeRandomHolder(3);
        paymentService = new PaymentService(paymentRepository,localDateTimeHolder,uuidRandomHolder,orderMessage,randomHolder);
    }

    @Test
    @DisplayName("랜덤한 값이 3이상이면 결제에 성공한다")
    void canSavePayment() throws Exception {
        // given
        LocalDateTime localDateTime = LocalDateTime.of(2025, 1, 2, 9, 1);
        localDateTimeHolder.setLocalDateTime(localDateTime);
        randomHolder.setValue(3);
        String orderId = "orderId";
        Long amount = 1000L;

        // when
        Payment result = paymentService.create(orderId, amount);
        int code = orderMessage.getCode();

        // then
        assertAll(() -> {
            assertThat(result.paymentId()).isEqualTo("random");
            assertThat(result.amount()).isEqualTo(amount);
            assertThat(result.orderId()).isEqualTo(orderId);
            assertThat(result.createdAt()).isEqualTo(localDateTime);
            assertThat(code).isEqualTo(SUCCESS.getCode());
        });

    }

    @Test
    @DisplayName("랜덤한 값이 2이하이면 결제에 실패한다")
    void failSavePayment() throws Exception {
        // given
        LocalDateTime localDateTime = LocalDateTime.of(2025, 1, 2, 9, 1);
        localDateTimeHolder.setLocalDateTime(localDateTime);
        randomHolder.setValue(2);
        String orderId = "orderId";
        Long amount = 1000L;

        // when
        Payment result = paymentService.create(orderId, amount);
        int code = orderMessage.getCode();

        // then
        assertAll(() -> {
            assertThat(result.paymentId()).isEqualTo("random");
            assertThat(result.amount()).isEqualTo(amount);
            assertThat(result.orderId()).isEqualTo(orderId);
            assertThat(result.createdAt()).isEqualTo(localDateTime);
            assertThat(code).isEqualTo(FAIL.getCode());
        });

    }
}