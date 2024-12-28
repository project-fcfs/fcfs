package hanghae.order_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hanghae.order_service.controller.resp.ResponseDto;
import hanghae.order_service.service.OrderService;
import hanghae.order_service.service.common.exception.CustomApiException;
import hanghae.order_service.service.common.util.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaMessageConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaMessageConsumer.class);
    private final ObjectMapper mapper;
    private final OrderService orderService;

    public KafkaMessageConsumer(ObjectMapper mapper, OrderService orderService) {
        this.mapper = mapper;
        this.orderService = orderService;
    }

    @KafkaListener(topics = "fcfs-order", groupId = "order-group")
    public void orderConfirm(String message) {
        log.info(message);
        OrderDecideReqDto response = convertToConfirm(message);
        orderService.decideOrder(response.code, response.orderId());
    }

    private OrderDecideReqDto convertToConfirm(String message) {
        try {
            ResponseDto response = mapper.readValue(message, ResponseDto.class);
            String orderId = (String) response.data();

            return new OrderDecideReqDto(response.code(), orderId);
        } catch (JsonProcessingException e) {
            throw new CustomApiException(ErrorMessage.ERROR_PARSE_JSON.getMessage());
        }
    }

    private record OrderDecideReqDto(
            int code,
            String orderId
    ) {
    }
}
