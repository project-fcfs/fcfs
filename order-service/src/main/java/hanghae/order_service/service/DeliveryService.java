package hanghae.order_service.service;

import hanghae.order_service.domain.order.Delivery;
import hanghae.order_service.domain.order.Order;
import hanghae.order_service.service.port.DeliveryRepository;
import hanghae.order_service.service.port.LocalDateTimeHolder;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeliveryService {
    /*private final DeliveryRepository deliveryRepository;
    private final LocalDateTimeHolder localDateTimeHolder;

    public DeliveryService(DeliveryRepository deliveryRepository, LocalDateTimeHolder localDateTimeHolder) {
        this.deliveryRepository = deliveryRepository;
        this.localDateTimeHolder = localDateTimeHolder;
    }

    @Transactional
    public void process(String address, Order savedOrder) {
        LocalDateTime currentDate = localDateTimeHolder.getCurrentDate();
        Delivery delivery = Delivery.create(address, savedOrder, currentDate);
        deliveryRepository.save(delivery);
    }*/
}
