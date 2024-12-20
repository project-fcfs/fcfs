package hanghae.order_service.domain.cart;

import hanghae.order_service.service.common.exception.CustomApiException;
import hanghae.order_service.service.common.util.ErrorMessage;

public record CartProduct(
        Long id,
        int quantity,
        String productId,
        Cart cart
) {

    public static CartProduct create(String productId, Cart cart) {
        return new CartProduct(null, 1, productId, cart);
    }

    public CartProduct updateCount(int count) {
        int remainQuantity = quantity + count;
        if (remainQuantity < 0) {
            throw new CustomApiException(ErrorMessage.OUT_OF_STOCK_CART.getMessage());
        }
        return new CartProduct(id, remainQuantity, productId, cart);
    }

}