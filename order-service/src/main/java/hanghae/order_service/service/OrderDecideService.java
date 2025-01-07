package hanghae.order_service.service;

import static hanghae.order_service.service.common.util.OrderConstant.ORDER_SUCCESS;

import hanghae.order_service.domain.order.Order;
import hanghae.order_service.domain.order.OrderProduct;
import hanghae.order_service.service.common.exception.CustomApiException;
import hanghae.order_service.service.common.util.ErrorMessage;
import hanghae.order_service.service.port.CartProductRepository;
import hanghae.order_service.service.port.LocalDateTimeHolder;
import hanghae.order_service.service.port.OrderProductMessage;
import hanghae.order_service.service.port.OrderRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderDecideService {
    private final LocalDateTimeHolder localDateTimeHolder;
    private final OrderRepository orderRepository;
    private final CartProductRepository cartProductRepository;
    private final OrderProductMessage orderProductMessage;

    public OrderDecideService(LocalDateTimeHolder localDateTimeHolder, OrderRepository orderRepository,
                              CartProductRepository cartProductRepository, OrderProductMessage orderProductMessage) {
        this.localDateTimeHolder = localDateTimeHolder;
        this.orderRepository = orderRepository;
        this.cartProductRepository = cartProductRepository;
        this.orderProductMessage = orderProductMessage;
    }

    /**
     * 재고 여부에 따라 주문 성공, 실패가 된다
     */
    @Transactional
    public void decideOrder(int code, String orderId) {
        Order order = orderRepository.findOrderById(orderId)
                .orElseThrow(() -> new CustomApiException(ErrorMessage.NOT_FOUND_ORDER.getMessage()));
        LocalDateTime currentDate = localDateTimeHolder.getCurrentDate();
        Order updatedOrder;

        if (code == ORDER_SUCCESS) {
            updatedOrder = processCompleteOrder(order, currentDate);
        } else {
            updatedOrder = processCancelOrder(order, currentDate);
        }

        orderRepository.save(updatedOrder);
    }

    /**
     * 주문 성공 시 주문완료로 바꾸고 해당하는 상품들을 장바구니에서 삭제한다
     */
    private Order processCompleteOrder(Order order, LocalDateTime currentDate) {
        Order completedOrder = order.completeOrder(currentDate);
        List<Long> productIds = completedOrder.orderProducts().stream().map(OrderProduct::productId).toList();
        String userId = order.userId();

        cartProductRepository.removeCartItems(productIds, userId);
        return completedOrder;
    }

    /**
     * 주문 실패 시 차감했던 재고를 원복한다
     */
    private Order processCancelOrder(Order order, LocalDateTime currentDate) {
        Order canceledOrder = order.cancelOrder(currentDate);
        orderProductMessage.restoreStock(canceledOrder.orderProducts());
        return canceledOrder;
    }
}
