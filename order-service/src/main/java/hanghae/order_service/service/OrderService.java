package hanghae.order_service.service;

import hanghae.order_service.domain.cart.CartProduct;
import hanghae.order_service.domain.order.Delivery;
import hanghae.order_service.domain.order.Order;
import hanghae.order_service.domain.order.OrderProduct;
import hanghae.order_service.service.common.exception.CustomApiException;
import hanghae.order_service.service.common.util.ErrorMessage;
import hanghae.order_service.service.port.CartProductRepository;
import hanghae.order_service.service.port.LocalDateTimeHolder;
import hanghae.order_service.service.port.OrderProductMessage;
import hanghae.order_service.service.port.OrderRepository;
import hanghae.order_service.service.port.ProductClient;
import hanghae.order_service.service.port.UuidRandomHolder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {
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
     * 장바구니 정보로 Product-service에서 가격, 정보 등을 가져온다
     * 주문을 하면 Pending 상태로 저장된다
     * Product-service로 orderId와 수량을 보내 재고여부를 판단하여 주문이 진행되거나 예외가 발생한다
     */
    @Transactional
    public Order order(List<String> productIds, String address, String userId) {
        String orderId = uuidRandomHolder.getRandomUuid();
        LocalDateTime currentDate = localDateTimeHolder.getCurrentDate();

        List<OrderProduct> orderProducts = generateOrderProducts(userId, productIds);

        Delivery delivery = Delivery.create(address, currentDate);
        Order order = Order.create(userId, orderId, orderProducts, delivery, currentDate);

        ResponseEntity<?> response = productClient.removeStock(order.orderProducts());
        if (response.getStatusCode().is2xxSuccessful()) {
            return orderRepository.save(order);
        }
        throw new CustomApiException(ErrorMessage.OUT_OF_STOCK.getMessage());
    }

    /**
     * productId가 같으면 장바구니의 수량으로 바꾸기 orderItems는 Product-service에서 가져오기 때문에 장바구니 속 유저가 저장한 수량으로 바꿔서 해당 수량만큼 주문을 진행함
     */
    private List<OrderProduct> generateOrderProducts(String userId, List<String> productIds) {
        LocalDateTime currentDate = localDateTimeHolder.getCurrentDate();

        Map<String, Integer> cartProducts = cartProductRepository.findByUserSelectedCart(userId, productIds)
                .stream().collect(Collectors.toMap(CartProduct::productId, CartProduct::quantity));

        if(cartProducts.isEmpty()) {
            throw new CustomApiException(ErrorMessage.NOT_FOUND_CART_PRODUCT.getMessage());
        }

        return productClient.getProducts(productIds).getBody().stream()
                .map(orderItem -> {
                    Integer count = cartProducts.get(orderItem.productId());
                    return OrderProduct.create(orderItem.price(), count, orderItem.productId(), currentDate);
                }).toList();
    }

    /**
     * 주문취소 가능한지 확인하고 주문 취소하기
     * 취소하고 원복하기
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
