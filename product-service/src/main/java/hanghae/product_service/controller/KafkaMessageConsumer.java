package hanghae.product_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hanghae.product_service.controller.req.StockUpdateReqDto;
import hanghae.product_service.service.common.exception.CustomApiException;
import hanghae.product_service.service.common.util.ErrorMessage;
import hanghae.product_service.service.lock.LuaStockService;
import hanghae.product_service.service.lock.PessimisticLockStockService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaMessageConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaMessageConsumer.class);
    private final PessimisticLockStockService pessimisticLockStockService;
    private final LuaStockService luaStockService;
    private final ObjectMapper mapper;

    public KafkaMessageConsumer(PessimisticLockStockService pessimisticLockStockService,
                                LuaStockService luaStockService,
                                ObjectMapper mapper) {
        this.pessimisticLockStockService = pessimisticLockStockService;
        this.luaStockService = luaStockService;
        this.mapper = mapper;
    }

    @KafkaListener(topics = "fcfs_order_restore", groupId = "product-group")
    public void productOrderRestore(String message) {
        log.info("fcfs_order_restore {}", message);
        List<StockUpdateReqDto> orderMessages = convertToRequest(message);
        luaStockService.restoreQuantity(orderMessages);
    }

    @KafkaListener(topics = "fcfs_order_remove", groupId = "product-group")
    public void productOrderProcess(String message) {
        log.info("fcfs_order_remove {}", message);
        List<StockUpdateReqDto> orderMessages = convertToRequest(message);
        luaStockService.processOrder(orderMessages);
    }

    private List<StockUpdateReqDto> convertToRequest(String value) {
        try {
            return mapper.readValue(value, new TypeReference<List<StockUpdateReqDto>>() {
            });
        } catch (JsonProcessingException e) {
            throw new CustomApiException(ErrorMessage.ERROR_PARSE_JSON.getMessage(), e);
        }
    }
}
