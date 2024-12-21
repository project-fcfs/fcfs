package hanghae.order_service.domain.order;

import static hanghae.order_service.service.common.util.OrderConstant.canReturnDay;

import hanghae.order_service.service.common.exception.CustomApiException;
import hanghae.order_service.service.common.util.ErrorMessage;
import java.time.LocalDateTime;

public record Delivery(
        Long id,
        String address,
        DeliveryStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    /**
     * 생성 메서드
     * 배달을 생성하면 배달준비중 상태가 된다
     */
    public static Delivery create(String address, LocalDateTime currentDate) {
        return new Delivery(null, address, DeliveryStatus.PREPARING, currentDate, currentDate);
    }

    /**
     * 배달준비중 상태일때 취소가 가능하다
     * 준비중이 아니라면 에러를 반환한다
     */
    public Delivery cancelIfPossible(LocalDateTime currentDate) {
        if (status.equals(DeliveryStatus.PREPARING)) {
            return new Delivery(id, address, DeliveryStatus.CANCELED, createdAt, currentDate);
        }
        throw new CustomApiException(ErrorMessage.ERROR_CANNOT_CANCEL_SHIPPING.getMessage());
    }

    /**
     * 배달완료 상태와 환불기한이 지나지 않았다면 취소가 가능하다
     * 둘 중 하나라도 안되면 에러를 반환한다
     */
    public Delivery refundIfEligible(LocalDateTime currentDate) {
        if (status.equals(DeliveryStatus.COMPLETED) && currentDate.isBefore(updatedAt.plusDays(canReturnDay))) {
            return new Delivery(id, address, DeliveryStatus.CANCELED, createdAt, currentDate);
        }
        throw new CustomApiException(ErrorMessage.ERROR_CANNOT_RETURN_SHIPPED.getMessage());
    }

    public Delivery cancelOutOfStock(LocalDateTime currentDate){
        return new Delivery(id, address, DeliveryStatus.CANCELED, createdAt, currentDate);
    }
}
