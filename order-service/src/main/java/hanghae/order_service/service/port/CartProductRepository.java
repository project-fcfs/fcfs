package hanghae.order_service.service.port;

import hanghae.order_service.domain.cart.CartProduct;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface CartProductRepository {
    void save(CartProduct cartProduct);
    Optional<CartProduct> findCartProduct(String productId, String userId);
    List<CartProduct> findAllByUserId(String userId);

    void removeCartItem(CartProduct cartProduct);

    List<CartProduct> findByUserSelectedCart(String userId, List<String> productIds);

    void clearItemsByOrder(List<String> productIds, String userId);
}
