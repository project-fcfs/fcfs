package hanghae.order_service.controller;

import hanghae.order_service.controller.docs.OrderControllerDocs;
import hanghae.order_service.controller.req.OrderCreateReqDto;
import hanghae.order_service.controller.req.OrderFcfsCreateReqDto;
import hanghae.order_service.controller.resp.OrderRespDto;
import hanghae.order_service.domain.order.Order;
import hanghae.order_service.service.OrderService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/orders")
@RestController
public class OrderController implements OrderControllerDocs {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * 카트에 있는 상품만 주문할 수 있다
     */
    @PostMapping
    public ResponseEntity<?> processOrder(@RequestHeader("userId") String userId,
                                          @RequestBody OrderCreateReqDto createReqDto, @RequestParam String address) {
        Order order = orderService.order(createReqDto.productIds(), address, userId);
        return new ResponseEntity<>(OrderRespDto.of(order), HttpStatus.CREATED);
    }

    /**
     * 선착순 구매는 구매하기 버튼으로만 구매할 수 있다
     */
    @PostMapping("/fcfs")
    public ResponseEntity<?> processFcfsOrder(@RequestHeader("userId") String userId,
                                              @RequestBody OrderFcfsCreateReqDto createReqDto,
                                              @RequestParam String address) {
        Order order = orderService.fcfsOrder(createReqDto.productId(), createReqDto.orderCount(), address, userId);
        return new ResponseEntity<>(OrderRespDto.of(order), HttpStatus.CREATED);
    }

    /**
     * 주문을 취소한다
     */
    @PostMapping("/cancel/{orderId}")
    public ResponseEntity<?> cancelOrder(@RequestHeader("userId") String userId,
                                         @PathVariable("orderId") String orderId) {
        orderService.cancel(userId, orderId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 환불을 요청한다
     */
    @PostMapping("/refund/{orderId}")
    public ResponseEntity<?> prefundOrder(@RequestHeader("userId") String userId,
                                          @PathVariable("orderId") String orderId) {
        orderService.processRefund(userId, orderId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 유저의 주문완료한 상품들을 가져온다
     */
    @GetMapping
    public ResponseEntity<?> fetchAllUserOrders(@RequestHeader("userId") String userId) {
        List<Order> orders = orderService.getUserOrderHistory(userId);

        List<OrderRespDto> orderResponse = orders.stream().map(OrderRespDto::of).toList();
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }


}
