package hanghae.order_service.service;

import hanghae.order_service.domain.order.Delivery;
import hanghae.order_service.domain.order.DeliveryStatus;
import hanghae.order_service.domain.order.Order;
import hanghae.order_service.domain.order.OrderStatus;
import hanghae.order_service.infrastructure.product.ItemRefund;
import hanghae.order_service.service.port.DeliveryRepository;
import hanghae.order_service.service.port.LocalDateTimeHolder;
import hanghae.order_service.service.port.OrderRepository;
import hanghae.order_service.service.port.OrderProductClient;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderScheduler {

    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;
    private final LocalDateTimeHolder localDateTimeHolder;
    private final OrderProductClient orderProductClient;

    public OrderScheduler(DeliveryRepository deliveryRepository, OrderRepository orderRepository,
                          LocalDateTimeHolder localDateTimeHolder,
                          OrderProductClient orderProductClient) {
        this.deliveryRepository = deliveryRepository;
        this.orderRepository = orderRepository;
        this.localDateTimeHolder = localDateTimeHolder;
        this.orderProductClient = orderProductClient;
    }

    /**
     * 배달준비중 상태가 하루가 되면 배송중으로 바꾼다
     */
    @Scheduled(cron = "* * 10 * * * ")
    public void processDelivery() {
        LocalDateTime currentDate = localDateTimeHolder.getCurrentDate();
        LocalDateTime dateWithMinusDay = currentDate.minusDays(1);
        List<Delivery> deliveries = deliveryRepository.findDeliveryStatusByDate(DeliveryStatus.PREPARING,
                dateWithMinusDay);
        List<Delivery> results = deliveries.stream()
                .map(i -> i.processDelivery(currentDate))
                .toList();
        deliveryRepository.saveAll(results);
    }

    /**
     * 배송중 상태가 하루가 지나면 배송완료로 바꾼다
     */
    @Scheduled(cron = "* * 10 * * * ")
    public void processCompleteDelivery() {
        LocalDateTime currentDate = localDateTimeHolder.getCurrentDate();
        LocalDateTime dateWithMinusDay = currentDate.minusDays(1);
        List<Delivery> deliveries = deliveryRepository.findDeliveryStatusByDate(DeliveryStatus.DELIVERING,
                dateWithMinusDay);
        List<Delivery> results = deliveries.stream()
                .map(i -> i.completeDelivery(currentDate))
                .toList();
        deliveryRepository.saveAll(results);
    }

    /**
     * 반품 신청 후 하루가 지나면 반품이 완료가 되고 재고에 반영된다
     */
    @Scheduled(cron = "* * 10 * * * ")
    public void completeRefund() {
        LocalDateTime currentDate = localDateTimeHolder.getCurrentDate();
        LocalDateTime dateWithMinusDay = currentDate.minusDays(1);

        // 환불 요청된 주문 목록 가져오기
        List<Order> orders = orderRepository.findAllRequestRefund(OrderStatus.RETURN_REQUESTED, dateWithMinusDay);

        // 환불 요청된 주문 목록 가져오기
        // 각 주문을 처리하고 환불 목록 및 주문 결과 생성
        List<ItemRefund> itemRefunds = new ArrayList<>();
        List<Order> orderResults = new ArrayList<>();

        for (Order order : orders) {
            // 환불 완료된 주문 생성
            Order completedRefund = order.completedRefund(currentDate);
            orderResults.add(completedRefund);

            // 주문 상품에 대한 환불 정보 생성
            order.orderProducts().forEach(orderProduct ->
                    itemRefunds.add(new ItemRefund(orderProduct.productId(), orderProduct.orderCount()))
            );
        }

        // 완료된 주문 저장 및 재고 업데이트 카프카 이용
        // openfeign은 데이터 소실 위험성이 있음
        orderProductClient.addProductStock(itemRefunds);
        orderRepository.saveAll(orderResults);
    }
}
