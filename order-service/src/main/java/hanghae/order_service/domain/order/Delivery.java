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

    public static Delivery create(String address, LocalDateTime currentDate) {
        return new Delivery(null, address, DeliveryStatus.PREPARING, currentDate, currentDate);
    }

    public Delivery cancelIfPossible(LocalDateTime currentDate) {
        if (status.equals(DeliveryStatus.PREPARING)) {
            return new Delivery(id, address, DeliveryStatus.CANCELED, createdAt, currentDate);
        }
        throw new CustomApiException(ErrorMessage.ERROR_CANNOT_CANCEL_SHIPPING.getMessage());
    }

    public Delivery refundIfEligible(LocalDateTime currentDate) {
        if (status.equals(DeliveryStatus.COMPLETED) && updatedAt.plusDays(canReturnDay).isBefore(currentDate)) {
            return new Delivery(id, address, DeliveryStatus.CANCELED, createdAt, currentDate);
        }
        throw new CustomApiException(ErrorMessage.ERROR_CANNOT_RETURN_SHIPPED.getMessage());
    }

    public Delivery cancelOutOfStock(LocalDateTime currentDate){
        return new Delivery(id, address, DeliveryStatus.CANCELED, createdAt, currentDate);
    }
}
