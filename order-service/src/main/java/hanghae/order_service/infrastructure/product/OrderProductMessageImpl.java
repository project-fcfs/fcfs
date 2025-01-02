package hanghae.order_service.infrastructure.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hanghae.order_service.domain.order.OrderProduct;
import hanghae.order_service.service.common.exception.CustomApiException;
import hanghae.order_service.service.common.util.ErrorMessage;
import hanghae.order_service.service.port.OrderProductMessage;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderProductMessageImpl implements OrderProductMessage {

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
    public void removeStock(List<OrderProduct> orderProducts, String orderId) {
        List<OrderMessage> orderMessages = orderProducts.stream()
                .map(i -> new OrderMessage(i.productId(), i.orderCount(), orderId)).toList();
        String message = convertOrderMessage(orderMessages);
        kafkaTemplate.send(removeTopic, message);
    }

    @Override
    public void restoreStock(List<OrderProduct> orderProducts) {
        List<OrderMessage> orderMessages = orderProducts.stream()
                .map(i -> new OrderMessage(i.productId(), i.orderCount(), null)).toList();
        String message = convertOrderMessage(orderMessages);
        kafkaTemplate.send(restoreTopic, message);
    }

    private String convertOrderMessage(List<OrderMessage> orderMessages) {
        try{
            return mapper.writeValueAsString(orderMessages);
        }catch (JsonProcessingException e) {
            throw new CustomApiException(ErrorMessage.ERROR_PARSE_JSON.getMessage());
        }
    }
}
