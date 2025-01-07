package hanghae.order_service.mock;

import hanghae.order_service.domain.cart.CartProduct;
import hanghae.order_service.service.port.CartProductRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class FakeCartProductRepository implements CartProductRepository {
    private List<CartProduct> data = new ArrayList<>();
    private AtomicLong counter = new AtomicLong();

    @Override
    public void save(CartProduct cartProduct) {
        if (cartProduct.id() == null || cartProduct.id() == 0L) {
            CartProduct newCartProduct = new CartProduct(counter.incrementAndGet(),
                    cartProduct.quantity(), cartProduct.productId(),
                    cartProduct.cart());
            data.add(newCartProduct);
        } else {
            data.removeIf(i -> i.id().equals(cartProduct.id()));
            data.add(cartProduct);
        }
    }

    @Override
    public Optional<CartProduct> findCartProduct(Long productId, String userId) {
        return data.stream().filter(i -> i.productId().equals(productId))
                .filter(i -> i.cart().userId().equals(userId)).findFirst();
    }

    @Override
    public List<CartProduct> findAllByUserId(String userId) {
        return data.stream().filter(i -> i.cart().userId().equals(userId)).toList();
    }

    @Override
    public List<CartProduct> findByUserSelectedCart(String userId, List<Long> productIds) {
        return data.stream().filter(i -> i.cart().userId().equals(userId))
                .filter(i -> productIds.contains(i.productId()))
                .toList();
    }

    @Override
    public void removeCartItems(List<Long> productIds, String userId) {
        data.removeIf(i ->
                i.cart().userId().equals(userId) &&
                        productIds.contains(i.productId())
        );
    }
}
