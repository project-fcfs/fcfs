package hanghae.order_service.service;

import hanghae.order_service.controller.req.OrderCreateReqDto;
import hanghae.order_service.domain.order.Delivery;
import hanghae.order_service.domain.order.Order;
import hanghae.order_service.domain.order.OrderProduct;
import hanghae.order_service.service.port.LocalDateTimeHolder;
import hanghae.order_service.service.port.OrderRepository;
import hanghae.order_service.service.port.UuidRandomHolder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final LocalDateTimeHolder localDateTimeHolder;
    private final UuidRandomHolder uuidRandomHolder;

    public OrderService(OrderRepository orderRepository,
                        LocalDateTimeHolder localDateTimeHolder, UuidRandomHolder uuidRandomHolder) {
        this.orderRepository = orderRepository;
        this.localDateTimeHolder = localDateTimeHolder;
        this.uuidRandomHolder = uuidRandomHolder;
    }

    @Transactional
    public void order(List<OrderCreateReqDto> createReqDtos, String address, String userId) {
        String orderId = uuidRandomHolder.getRandomUuid();
        LocalDateTime currentDate = localDateTimeHolder.getCurrentDate();
        List<OrderProduct> orderProducts = new ArrayList<>();

        for (OrderCreateReqDto orderDto : createReqDtos) {
            // todo Product정보 가져오기
            OrderProduct orderProduct = OrderProduct.create(100, orderDto.count(), orderDto.productId(),
                    localDateTimeHolder.getCurrentDate());
            orderProducts.add(orderProduct);
        }
        Delivery delivery = Delivery.create(address, currentDate);
        Order order = Order.create(userId, orderId, orderProducts, delivery, currentDate);
        Order savedOrder = orderRepository.save(order);

        // todo product-service에서 재고 여부 확인하고 확정 및 취소하기
    }

    @Transactional
    public void cancel(String userId, String orderId) {
        // todo 주문취소 가능한지 확인하고 주문 취소하기

    }

    @Transactional
    public void refund(String userId, String orderId) {
        // todo 환불요청이 가능한지 확인하고 환불하기

    }


}
