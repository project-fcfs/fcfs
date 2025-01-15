package hanghae.order_service.domain.order;

import static hanghae.order_service.service.common.util.OrderConstant.*;

import hanghae.order_service.service.common.exception.CustomApiException;
import hanghae.order_service.service.common.exception.ErrorCode;
import hanghae.order_service.service.common.util.TimeFormatter;
import java.time.LocalDateTime;

public record Delivery(
        Long id,
        String address,
        DeliveryStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    /**
     * 생성 메서드 배달을 생성하면 배달준비중 상태가 된다
     */
    public static Delivery create(String address, LocalDateTime currentDate) {
        return new Delivery(null, address, DeliveryStatus.PREPARING, currentDate, currentDate);
    }

    /**
     * 배달준비중 상태일때 취소가 가능하다 준비중이 아니라면 에러를 반환한다
     */
    public Delivery cancelIfPossible(LocalDateTime currentDate) {
        if (status.equals(DeliveryStatus.PREPARING)) {
            return new Delivery(id, address, DeliveryStatus.CANCELED, createdAt, currentDate);
        }
        throw new CustomApiException(ErrorCode.ERROR_CANNOT_CANCEL_SHIPPING, TimeFormatter.formatTime(currentDate));
    }

    /**
     * 배달완료 상태와 환불기한이 지나지 않았다면 취소가 가능하다 둘 중 하나라도 안되면 에러를 반환한다
     */
    public Delivery refundIfEligible(LocalDateTime currentDate) {
        if (status.equals(DeliveryStatus.COMPLETED) && currentDate.isBefore(updatedAt.plusDays(RETURN_DAY))) {
            return new Delivery(id, address, DeliveryStatus.CANCELED, createdAt, currentDate);
        }
        throw new CustomApiException(ErrorCode.ERROR_CANNOT_RETURN_SHIPPED, TimeFormatter.formatTime(currentDate));
    }

    /**
     * 재고부족으로 인한 취소 또는 그냥 취소
     */
    public Delivery cancelOutOfStock(LocalDateTime currentDate) {
        return new Delivery(id, address, DeliveryStatus.CANCELED, createdAt, currentDate);
    }

    public Delivery processDelivery(LocalDateTime currentDate) {
        return new Delivery(id, address, DeliveryStatus.DELIVERING, createdAt, currentDate);
    }

    public Delivery completeDelivery(LocalDateTime currentDate) {
        return new Delivery(id, address, DeliveryStatus.COMPLETED, createdAt, currentDate);
    }
}
