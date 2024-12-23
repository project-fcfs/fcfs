package hanghae.order_service.controller;

import hanghae.order_service.domain.order.Order;
import hanghae.order_service.service.OrderService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/order")
@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * 카트에 있는 상품만 주문할 수 있다
     */
    @PostMapping
    public ResponseEntity<?> processOrder(@RequestHeader("userId") String userId,
                                          @RequestParam List<String> productIds, @RequestParam String address) {
        Order order = orderService.order(productIds, address, userId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 주문을 취소한다
     */
    @PostMapping("/cancel")
    public ResponseEntity<?> cancelOrder(@RequestHeader("userId") String userId,
                                         @RequestParam("orderId") String orderId) {
        orderService.cancel(userId,orderId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
