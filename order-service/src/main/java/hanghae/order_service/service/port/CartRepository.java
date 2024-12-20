package hanghae.order_service.service.port;

import hanghae.order_service.domain.cart.Cart;
import java.util.Optional;

public interface CartRepository {

    Cart save(Cart cart);
    Optional<Cart> findByUserId(String userId);
}
