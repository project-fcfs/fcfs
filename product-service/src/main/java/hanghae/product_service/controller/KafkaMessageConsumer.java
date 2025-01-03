package hanghae.product_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hanghae.product_service.controller.req.OrderMessageReqDto;
import hanghae.product_service.service.ProductStockService;
import hanghae.product_service.service.common.exception.CustomApiException;
import hanghae.product_service.service.common.util.ErrorMessage;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaMessageConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaMessageConsumer.class);
    private final ProductStockService productStockService;
    private final ObjectMapper mapper;

    public KafkaMessageConsumer(ProductStockService productStockService, ObjectMapper mapper) {
        this.productStockService = productStockService;
        this.mapper = mapper;
    }

    @KafkaListener(topics = "fcfs_order_restore", groupId = "product-group")
    public void productOrderRestore(String message) {
        log.info("fcfs_order_restore {}", message);
        List<OrderMessageReqDto> orderMessages = convertToRequest(message);
        productStockService.restoreQuantity(orderMessages);
    }

    private List<OrderMessageReqDto> convertToRequest(String value) {
        try {
            return mapper.readValue(value, new TypeReference<List<OrderMessageReqDto>>() {
            });
        } catch (JsonProcessingException e) {
            throw new CustomApiException(ErrorMessage.ERROR_PARSE_JSON.getMessage(), e);
        }
    }
}
