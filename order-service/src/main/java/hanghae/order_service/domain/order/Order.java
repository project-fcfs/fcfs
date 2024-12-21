package hanghae.order_service.domain.order;

import hanghae.order_service.service.common.exception.CustomApiException;
import hanghae.order_service.service.common.util.ErrorMessage;
import java.time.LocalDateTime;
import java.util.List;

public record Order(
        Long id,
        OrderStatus orderStatus,
        String userId,
        String uuid,
        List<OrderProduct> orderProducts,
        Delivery delivery,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    /**
     * 주문을 생성한다
     * 주문을 생성하면 재고여부를 확인하기 위해 일단 대기상태로 저장한다
     */
    public static Order create(String userId, String uuid, List<OrderProduct> orderProducts,
                               Delivery delivery,LocalDateTime currentDate) {
        return new Order(null, OrderStatus.PENDING, userId, uuid,
                orderProducts, delivery, currentDate, currentDate);
    }

    /**
     * 주문을 취소한다
     * 배달이 준비중일 때 취소가 가능하다
     */
    public Order cancelOrder(LocalDateTime currentDate){
        Delivery canceldDelivery = delivery.cancelIfPossible(currentDate);
        return new Order(id, OrderStatus.CANCELLED, userId, uuid, orderProducts,
                canceldDelivery, createdAt, currentDate);
    }

    /**
     * 환불 요청을 한다
     * 주문완료상태와 배달완료가 되고 환불기한 내라면 환불요청이 가능하다
     */
    public Order requestRefund(LocalDateTime currentDate){
        if (orderStatus.equals(OrderStatus.COMPLETED)) {
            Delivery returnDelivery = delivery.refundIfEligible(currentDate);
            return new Order(id, OrderStatus.RETURN_REQUESTED, userId, uuid, orderProducts,
                    returnDelivery, createdAt, currentDate);
        }
        throw new CustomApiException(ErrorMessage.ERROR_REQUEST_REFUND.getMessage());
    }

    /**
     * 환불 요청상태에서 일정기한이 지나면 환불완료 상태가 된다
     */
    public Order completedRefund(LocalDateTime currentDate){
        if (orderStatus.equals(OrderStatus.RETURN_REQUESTED)) {
            Delivery returnDelivery = delivery.refundIfEligible(currentDate);
            return new Order(id, OrderStatus.RETURN_COMPLETED, userId, uuid, orderProducts,
                    returnDelivery, createdAt, currentDate);
        }
        throw new CustomApiException(ErrorMessage.ERROR_COMPLETE_REFUND.getMessage());
    }

    /**
     * 상품의 재고가 없으면 취소상태가 된다
     */
    public Order handlerOutOfStock(LocalDateTime currentDate){
        Delivery canceldDelivery = delivery.cancelOutOfStock(currentDate);
        return new Order(id, OrderStatus.CANCELLED, userId, uuid, orderProducts,
                canceldDelivery, createdAt, currentDate);
    }

    /**
     * 조회 로직
     */
    public int getTotalPrice(){
        return orderProducts.stream()
                .mapToInt(OrderProduct::getTotalPrice)
                .sum();
    }

}
