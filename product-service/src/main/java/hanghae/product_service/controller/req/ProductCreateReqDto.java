package hanghae.product_service.controller.req;

public record ProductCreateReqDto(
        String name,
        int price,
        int quantity
) {
}
