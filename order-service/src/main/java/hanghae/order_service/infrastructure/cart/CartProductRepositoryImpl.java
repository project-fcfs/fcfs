package hanghae.order_service.infrastructure.cart;

import hanghae.order_service.domain.cart.CartProduct;
import hanghae.order_service.service.port.CartProductRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class CartProductRepositoryImpl implements CartProductRepository {
    private final CartProductJpaRepository jpaRepository;

    public CartProductRepositoryImpl(CartProductJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(CartProduct cartProduct) {

    }

    @Override
    public List<CartProduct> findAllByUserId(String userId) {
        return List.of();
    }

    @Override
    public Optional<CartProduct> findCartProduct(String productId, String userId) {
        return Optional.empty();
    }

    @Override
    public void removeCartItem(CartProduct cartProduct) {

    }

    @Override
    public List<CartProduct> findByUserSelectedCart(String userId, List<String> productIds) {
        return List.of();
    }
}
