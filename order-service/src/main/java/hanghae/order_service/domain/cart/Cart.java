package hanghae.order_service.domain.cart;

public record Cart(
        Long id,
        String userId
) {

    public static Cart create(String userId){
        return new Cart(null,userId);
    }
}
