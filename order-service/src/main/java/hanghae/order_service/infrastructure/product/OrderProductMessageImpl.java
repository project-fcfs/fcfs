package hanghae.order_service.infrastructure.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hanghae.order_service.domain.order.OrderProduct;
import hanghae.order_service.service.common.exception.CustomApiException;
import hanghae.order_service.service.common.util.ErrorMessage;
import hanghae.order_service.service.port.OrderProductMessage;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Component
public class OrderProductMessageImpl implements OrderProductMessage {

    private static final Logger log = LoggerFactory.getLogger(OrderProductMessageImpl.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String removeTopic;
    private final String restoreTopic;
    private final ObjectMapper mapper;

    public OrderProductMessageImpl(KafkaTemplate<String, String> kafkaTemplate,
                                   @Value("${order.topic.remove}") String removeTopic,
                                   @Value("${order.topic.restore}") String restoreTopic,
                                   ObjectMapper mapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.removeTopic = removeTopic;
        this.restoreTopic = restoreTopic;
        this.mapper = mapper;
    }

    @Override
    public void removeStock(List<OrderProduct> orderProducts) {
        List<OrderMessage> orderMessages = orderProducts.stream()
                .map(i -> new OrderMessage(i.productId(), i.orderCount())).toList();
        String message = convertOrderMessage(orderMessages);
        kafkaTemplate.send(removeTopic, message);
    }

    @Override
    public void restoreStock(List<OrderProduct> orderProducts) {
        List<OrderMessage> orderMessages = orderProducts.stream()
                .map(i -> new OrderMessage(i.productId(), i.orderCount())).toList();
        String message = convertOrderMessage(orderMessages);
        CompletableFuture<SendResult<String, String>> send = kafkaTemplate.send(restoreTopic, message);
        send.thenAccept(result -> {
            log.info("Message sent successfully to topic: {} at offset {}", result.getRecordMetadata().topic(),
                    result.getRecordMetadata().offset());
        }).exceptionally(ex -> {
            log.error("Failed to send message to topic {}, message {}", restoreTopic, message);
            throw new CustomApiException("Failed to send message to topic " + restoreTopic, ex);
        });
    }

    private String convertOrderMessage(List<OrderMessage> orderMessages) {
        try{
            return mapper.writeValueAsString(orderMessages);
        }catch (JsonProcessingException e) {
            throw new CustomApiException(ErrorMessage.ERROR_PARSE_JSON.getMessage());
        }
    }
}
