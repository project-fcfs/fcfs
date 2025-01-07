package hanghae.order_service.controller.req;

import java.util.List;

public record OrderCreateReqDto(
        List<Long> productIds
) {
}
