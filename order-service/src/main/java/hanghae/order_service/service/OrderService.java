package hanghae.order_service.service;

import hanghae.order_service.controller.resp.ResponseDto;
import hanghae.order_service.domain.cart.CartProduct;
import hanghae.order_service.domain.order.Delivery;
import hanghae.order_service.domain.order.Order;
import hanghae.order_service.domain.order.OrderProduct;
import hanghae.order_service.domain.product.Product;
import hanghae.order_service.service.common.exception.CustomApiException;
import hanghae.order_service.service.common.util.ErrorMessage;
import hanghae.order_service.service.common.util.OrderConstant;
import hanghae.order_service.service.port.CartProductRepository;
import hanghae.order_service.service.port.LocalDateTimeHolder;
import hanghae.order_service.service.port.OrderProductMessage;
import hanghae.order_service.service.port.OrderRepository;
import hanghae.order_service.service.port.ProductClient;
import hanghae.order_service.service.port.UuidRandomHolder;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    private final CartProductRepository cartProductRepository;
    private final LocalDateTimeHolder localDateTimeHolder;
    private final UuidRandomHolder uuidRandomHolder;
    private final ProductClient productClient;
    private final OrderProductMessage orderProductMessage;

    public OrderService(OrderRepository orderRepository, CartProductRepository cartProductRepository,
                        LocalDateTimeHolder localDateTimeHolder, UuidRandomHolder uuidRandomHolder,
                        ProductClient productClient, OrderProductMessage orderProductMessage) {
        this.orderRepository = orderRepository;
        this.cartProductRepository = cartProductRepository;
        this.localDateTimeHolder = localDateTimeHolder;
        this.uuidRandomHolder = uuidRandomHolder;
        this.productClient = productClient;
        this.orderProductMessage = orderProductMessage;
    }

    /**
     * 장바구니 정보로 Product-service에서 가격, 정보 등을 가져온다 주문을 하면 Pending 상태로 저장된다 Product-service로 orderId와 수량을 보내 재고여부를 판단하여 주문이
     * 진행되거나 예외가 발생한다
     */
    @Transactional
    public Order order(List<Long> productIds, String address, String userId) {
        String orderId = uuidRandomHolder.getRandomUuid();
        LocalDateTime currentDate = localDateTimeHolder.getCurrentDate();

        Map<Long, Integer> cartProducts = cartProductRepository.findByUserSelectedCart(userId, productIds)
                .stream().collect(Collectors.toMap(CartProduct::productId, CartProduct::quantity));

        if (cartProducts.isEmpty()) {
            throw new CustomApiException(ErrorMessage.NOT_FOUND_CART_PRODUCT.getMessage());
        }

        List<OrderProduct> orderProducts = generateOrderProducts(cartProducts, currentDate);

        Order order = generateOrder(address, userId, currentDate, orderId, orderProducts);

        return orderRepository.save(order);
    }

    private Order generateOrder(String address, String userId, LocalDateTime currentDate, String orderId,
                                List<OrderProduct> orderProducts) {
        Delivery delivery = Delivery.create(address, currentDate);
        return Order.create(userId, orderId, orderProducts, delivery, currentDate);
    }

    /**
     * productId가 같으면 장바구니의 수량으로 바꾸기 orderItems는 Product-service에서 가져오기 때문에 장바구니 속 유저가 저장한 수량으로 바꿔서 해당 수량만큼 주문을 진행함
     */
    private List<OrderProduct> generateOrderProducts(Map<Long, Integer> value, LocalDateTime currentDate) {

        ResponseDto<List<Product>> responseDto = productClient.processOrder(value);

        if (responseDto.code() == OrderConstant.ORDER_FAIL) {
            throw new CustomApiException(ErrorMessage.OUT_OF_STOCK.getMessage());
        }
        return responseDto.data().stream()
                .map(i -> {
                    Integer orderCount = value.get(i.productId());
                    return OrderProduct.create(i.price(), orderCount, i.productId(), currentDate);
                }).toList();
    }

    /**
     * 선착순 구매 선착순 구매는 오픈시간이 있고, 해당 오픈시간이 되지 않으면 구매할 수 없다
     */
    @Transactional
    public Order fcfsOrder(Long productId, int orderCount, String address, String userId) {
        LocalDateTime currentDate = localDateTimeHolder.getCurrentDate();
        String orderId = uuidRandomHolder.getRandomUuid();
        LocalTime currentTime = currentDate.toLocalTime();

        if (currentTime.isBefore(OrderConstant.OPEN_TIME)) {
            throw new CustomApiException(ErrorMessage.NOT_OPEN_TIME.getMessage());
        }
        Map<Long, Integer> orders = new HashMap<>();
        orders.put(productId, orderCount);
        List<OrderProduct> orderProducts = generateOrderProducts(orders, currentDate);
        // todo 레디스에서 재고감소 시에는 DB에도 재고감소 이벤트를 발송해야한다 안쓰면 삭제
        orderProductMessage.removeStock(orderProducts);

        Order order = generateOrder(address, userId, currentDate, orderId, orderProducts);
        return orderRepository.save(order);
    }

    /**
     * 주문취소 가능한지 확인하고 주문 취소하기 취소하고 원복하기
     */
    @Transactional
    public void cancel(String userId, String orderId) {
        Order order = getUserOrder(userId, orderId);
        Order canceldOrder = order.cancelOrder(localDateTimeHolder.getCurrentDate());
        Order savedOrder = orderRepository.save(canceldOrder);
        orderProductMessage.restoreStock(savedOrder.orderProducts());
    }

    /**
     * 환불요청이 가능한지 확인하고 환불요청하기
     */
    @Transactional
    public void processRefund(String userId, String orderId) {
        Order order = getUserOrder(userId, orderId);
        Order refundOrder = order.requestRefund(localDateTimeHolder.getCurrentDate());
        orderRepository.save(refundOrder);
    }

    /**
     * 유저가 주문한 오더를 가져온다
     */
    private Order getUserOrder(String userId, String orderId) {
        return orderRepository.findByUserOrderByOrderId(userId, orderId).orElseThrow(() ->
                new CustomApiException(ErrorMessage.NOT_FOUND_ORDER.getMessage()));
    }

    /**
     * 유저 주문한 모든 주문을 가져온다
     */
    public List<Order> getUserOrderHistory(String userId) {
        return orderRepository.findAllUserOrders(userId);
    }


}
