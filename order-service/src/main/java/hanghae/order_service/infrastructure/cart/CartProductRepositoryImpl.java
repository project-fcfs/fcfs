package hanghae.order_service.infrastructure.cart;

import hanghae.order_service.domain.cart.CartProduct;
import hanghae.order_service.service.port.CartProductRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.stereotype.Repository;

@Repository
public class CartProductRepositoryImpl implements CartProductRepository {
    private final CartProductJpaRepository jpaRepository;

    public CartProductRepositoryImpl(CartProductJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(CartProduct cartProduct) {
        jpaRepository.save(CartProductEntity.fromModel(cartProduct));
    }

    @Override
    public List<CartProduct> findAllByUserId(String userId) {
        return jpaRepository.findAllByUserId(userId).stream()
                .map(CartProductEntity::toModel).toList();
    }

    @Override
    public Optional<CartProduct> findCartProduct(Long productId, String userId) {
        return jpaRepository.findProductByProductAndUser(productId, userId).map(CartProductEntity::toModel);
    }

    @Override
    public List<CartProduct> findByUserSelectedCart(String userId, List<Long> productIds) {
        return jpaRepository.findAllCartProduct(userId, productIds)
                .stream().map(CartProductEntity::toModel).toList();
    }

    @Override
    public void removeCartItems(List<Long> productIds, String userId) {
        jpaRepository.deleteAllOrderProducts(productIds, userId);
    }
}
