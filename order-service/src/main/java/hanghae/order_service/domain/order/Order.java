package hanghae.order_service.domain.order;

import hanghae.order_service.service.common.exception.CustomApiException;
import hanghae.order_service.service.common.exception.ErrorCode;
import java.time.LocalDateTime;
import java.util.List;

public record Order(
        Long id,
        OrderStatus orderStatus,
        String userId,
        String orderId,
        List<OrderProduct> orderProducts,
        Delivery delivery,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    /**
     * 주문을 생성한다 주문을 생성하면 재고여부를 확인하기 위해 일단 대기상태로 저장한다
     */
    public static Order create(String userId, String orderId, List<OrderProduct> orderProducts,
                               Delivery delivery, LocalDateTime currentDate) {
        return new Order(null, OrderStatus.PENDING, userId, orderId,
                orderProducts, delivery, currentDate, currentDate);
    }

    /**
     * 재고가 없거나, 배송준비 중일때 주문을 취소할 수 있다
     */
    public Order cancelOrder(LocalDateTime currentDate) {
        Delivery canceledDelivery = delivery.cancelOutOfStock(currentDate);
        if (orderStatus.equals(OrderStatus.PENDING) || orderStatus.equals(OrderStatus.COMPLETED)) {
            return new Order(id, OrderStatus.CANCELLED, userId, orderId, orderProducts, canceledDelivery, createdAt, currentDate);
        }
        throw new CustomApiException(ErrorCode.ERROR_CANNOT_CANCEL_SHIPPING);
    }

    /**
     * 환불 요청을 한다 주문완료상태와 배달완료가 되고 환불기한 내라면 환불요청이 가능하다
     */
    public Order requestRefund(LocalDateTime currentDate) {
        Delivery refundedDelivery = delivery.refundIfEligible(currentDate);
        if (orderStatus.equals(OrderStatus.COMPLETED)) {
            return new Order(id, OrderStatus.RETURN_REQUESTED, userId, orderId,
                    orderProducts, refundedDelivery, createdAt, currentDate);
        }
        throw new CustomApiException(ErrorCode.ERROR_REQUEST_REFUND);
    }

    /**
     * 환불 요청상태라면 환불 완료 상태가 될 수 있다
     */
    public Order completedRefund(LocalDateTime currentDate) {
        if (orderStatus.equals(OrderStatus.RETURN_REQUESTED)) {
            return new Order(id, OrderStatus.RETURN_COMPLETED, userId, orderId,
                    orderProducts, delivery, createdAt, currentDate);
        }
        throw new CustomApiException(ErrorCode.ERROR_COMPLETE_REFUND);
    }

    /**
     * 재고가 존재하면 성공적으로 주문을 완료한다
     */
    public Order completeOrder(LocalDateTime currentDate) {
        return new Order(id, OrderStatus.COMPLETED, userId, orderId, orderProducts, delivery, createdAt, currentDate);
    }

    /**
     * 조회 로직
     */
    public Long getTotalPrice() {
        return (long) orderProducts.stream()
                .mapToInt(OrderProduct::getTotalPrice)
                .sum();
    }

}
