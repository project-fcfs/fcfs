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

    @Scheduled(cron = "* * 10 * * * ")
    public void processDelivery(){
        // todo 배달준비중 상태가 하루가 되면 배송중으로 바꾼다
    }

    @Scheduled(cron = "* * 10 * * * ")
    public void processCompleteDelivery(){
        // todo 배송중 상태가 하루가 지나면 배송완료로 바꾼다
    }
}
