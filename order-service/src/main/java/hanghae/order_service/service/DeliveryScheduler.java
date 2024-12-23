package hanghae.order_service.service;

import hanghae.order_service.service.port.DeliveryRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DeliveryScheduler {

    private final DeliveryRepository deliveryRepository;

    public DeliveryScheduler(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    /**
     * 배달준비중 상태가 하루가 되면 배송중으로 바꾼다
     */
    @Scheduled(cron = "* * 10 * * * ")
    public void processDelivery(){

    }

    /**
     *  배송중 상태가 하루가 지나면 배송완료로 바꾼다
     */
    @Scheduled(cron = "* * 10 * * * ")
    public void processCompleteDelivery(){

    }

    /**
     *  반품 신청 후 하루가 지나면 반품이 완료가 되고 재고에 반영된다
     */
    @Scheduled(cron = "* * 10 * * * ")
    public void completeRefund(){

    }
}
