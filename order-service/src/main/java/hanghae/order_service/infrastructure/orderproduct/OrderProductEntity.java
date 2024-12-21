package hanghae.order_service.infrastructure.orderproduct;

import java.time.LocalDateTime;

public class OrderProductEntity {

    private Long id;
    private int orderPrice;
    private int orderCount;
    private String productId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
