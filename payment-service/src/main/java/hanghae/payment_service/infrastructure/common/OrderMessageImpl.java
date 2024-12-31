package hanghae.payment_service.infrastructure.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hanghae.payment_service.controller.resp.ResponseDto;
import hanghae.payment_service.service.common.exception.CustomApiException;
import hanghae.payment_service.service.common.util.ErrorMessage;
import hanghae.payment_service.service.port.OrderMessage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderMessageImpl implements OrderMessage {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper mapper;

    public OrderMessageImpl(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper mapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.mapper = mapper;
    }

    @Override
    public void sendOrderDecide(int code, String message, String orderId) {
        ResponseDto<String> responseDto = new ResponseDto<>(code, message, orderId);
        String responseMessage = convertSendMessage(responseDto);
        kafkaTemplate.send("temp", responseMessage);
    }

    private String convertSendMessage(ResponseDto<String> responseDto) {
        try {
            return mapper.writeValueAsString(responseDto);
        } catch (JsonProcessingException e) {
            throw new CustomApiException(ErrorMessage.ERROR_PARSE_JSON.getMessage());
        }
    }
}
