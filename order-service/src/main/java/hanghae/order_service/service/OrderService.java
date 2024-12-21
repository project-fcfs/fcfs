package hanghae.order_service.service;

import hanghae.order_service.controller.req.OrderCreateReqDto;
import hanghae.order_service.domain.order.Delivery;
import hanghae.order_service.domain.order.Order;
import hanghae.order_service.domain.order.OrderProduct;
import hanghae.order_service.service.port.DeliveryRepository;
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
    private final DeliveryRepository deliveryRepository;
    private final LocalDateTimeHolder localDateTimeHolder;
    private final UuidRandomHolder uuidRandomHolder;

    public OrderService(OrderRepository orderRepository, DeliveryRepository deliveryRepository,
                        LocalDateTimeHolder localDateTimeHolder, UuidRandomHolder uuidRandomHolder) {
        this.orderRepository = orderRepository;
        this.deliveryRepository = deliveryRepository;
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
        Order order = Order.create(userId, orderId, orderProducts, currentDate);
        Order savedOrder = orderRepository.save(order);
        addDelivery(address, savedOrder);
    }

    private void addDelivery(String address, Order savedOrder) {
        LocalDateTime currentDate = localDateTimeHolder.getCurrentDate();
        Delivery delivery = Delivery.create(address, savedOrder, currentDate);
        deliveryRepository.save(delivery);
    }
}
