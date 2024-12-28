package hanghae.product_service.infrastrcuture.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hanghae.product_service.controller.resp.ResponseDto;
import hanghae.product_service.service.common.exception.CustomApiException;
import hanghae.product_service.service.common.util.ErrorMessage;
import hanghae.product_service.service.port.OrderProductMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderProductMessageImpl<T> implements OrderProductMessage<T> {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String orderDecideTopic;
    private final ObjectMapper mapper;

    public OrderProductMessageImpl(KafkaTemplate<String, String> kafkaTemplate,
                                   @Value("${order.topic.decide}") String orderDecideTopic, ObjectMapper mapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.orderDecideTopic = orderDecideTopic;
        this.mapper = mapper;
    }

    @Override
    public void sendResult(int code, String value, T data) {
        ResponseDto<Object> response = new ResponseDto<>(code, value, data);
        String message = convertToResponse(response);
        kafkaTemplate.send(orderDecideTopic, message);
    }

    private String convertToResponse(ResponseDto<?> response){
        try{
            return mapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            throw new CustomApiException(ErrorMessage.ERROR_PARSE_JSON.getMessage());
        }
    }
}
