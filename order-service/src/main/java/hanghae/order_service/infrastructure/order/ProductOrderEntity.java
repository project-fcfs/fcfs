package hanghae.order_service.infrastructure.order;

import java.time.LocalDateTime;

public class ProductOrderEntity {

    private Long id;
    private int orderPrice;
    private int orderCount;
    private String productId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
