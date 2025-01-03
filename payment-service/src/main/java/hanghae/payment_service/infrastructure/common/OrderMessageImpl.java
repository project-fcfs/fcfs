package hanghae.payment_service.infrastructure.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hanghae.payment_service.controller.resp.ResponseDto;
import hanghae.payment_service.service.common.exception.CustomApiException;
import hanghae.payment_service.service.common.util.ErrorMessage;
import hanghae.payment_service.service.port.OrderMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderMessageImpl implements OrderMessage {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper mapper;
    private final String orderDecideTopic;

    public OrderMessageImpl(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper mapper, @Value("${order.topic.decide}") String orderDecideTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.mapper = mapper;
        this.orderDecideTopic = orderDecideTopic;
    }

    @Override
    public void sendOrderDecide(int code, String message, String orderId, HttpStatus httpStatus) {
        ResponseDto<String> responseDto = new ResponseDto<>(code, message, orderId, httpStatus);
        String responseMessage = convertSendMessage(responseDto);
        kafkaTemplate.send(orderDecideTopic, responseMessage);
    }

    private String convertSendMessage(ResponseDto<String> responseDto) {
        try {
            return mapper.writeValueAsString(responseDto);
        } catch (JsonProcessingException e) {
            throw new CustomApiException(ErrorMessage.ERROR_PARSE_JSON.getMessage());
        }
    }
}
