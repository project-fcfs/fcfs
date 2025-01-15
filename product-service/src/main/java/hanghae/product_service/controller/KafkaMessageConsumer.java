package hanghae.product_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hanghae.product_service.controller.req.StockUpdateReqDto;
import hanghae.product_service.service.common.exception.CustomApiException;
import hanghae.product_service.service.common.exception.ErrorCode;
import hanghae.product_service.service.lock.LuaStockService;
import hanghae.product_service.service.lock.NamedLockStockFacade;
import hanghae.product_service.service.lock.PessimisticLockStockService;
import hanghae.product_service.service.lock.RedissonLockStockFacade;
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
    private final RedissonLockStockFacade redissonLockStockFacade;
    private final NamedLockStockFacade namedLockStockFacade;

    public KafkaMessageConsumer(PessimisticLockStockService pessimisticLockStockService,
                                LuaStockService luaStockService,
                                ObjectMapper mapper, RedissonLockStockFacade redissonLockStockFacade,
                                NamedLockStockFacade namedLockStockFacade) {
        this.pessimisticLockStockService = pessimisticLockStockService;
        this.luaStockService = luaStockService;
        this.mapper = mapper;
        this.redissonLockStockFacade = redissonLockStockFacade;
        this.namedLockStockFacade = namedLockStockFacade;
    }

    @KafkaListener(topics = "fcfs_order_restore", groupId = "product-group")
    public void productOrderRestore(String message) {
        log.info("fcfs_order_restore {}", message);
        List<StockUpdateReqDto> orderMessages = convertToRequest(message);
        pessimisticLockStockService.restoreQuantity(orderMessages);
    }

    @KafkaListener(topics = "fcfs_order_remove", groupId = "product-group")
    public void productOrderProcess(String message) {
        log.info("fcfs_order_remove {}", message);
        List<StockUpdateReqDto> orderMessages = convertToRequest(message);
        pessimisticLockStockService.processOrder(orderMessages);
    }

    private List<StockUpdateReqDto> convertToRequest(String value) {
        try {
            return mapper.readValue(value, new TypeReference<List<StockUpdateReqDto>>() {
            });
        } catch (JsonProcessingException e) {
            throw new CustomApiException(ErrorCode.ERROR_PARSE_DATA, e);
        }
    }
}
