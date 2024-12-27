package hanghae.order_service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaMessageConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaMessageConsumer.class);

    @KafkaListener(topics = "fcfs-order", groupId = "order-group")
    public void orderConfirm(String message) {
        log.info(message);
    }
}
