package hanghae.order_service.infrastructure.cart;

import hanghae.order_service.domain.cart.Cart;
import hanghae.order_service.service.port.CartRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class CartRepositoryImpl implements CartRepository {

    private final CartJpaRepository jpaRepository;

    public CartRepositoryImpl(CartJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Cart save(Cart cart) {
        return jpaRepository.save(CartEntity.fromModel(cart)).toModel();
    }

    @Override
    public Optional<Cart> findByUserId(String userId) {
        return Optional.empty();
    }
}
