package hanghae.order_service.mock;

import hanghae.order_service.domain.cart.Cart;
import hanghae.order_service.service.port.CartRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class FakeCartRepository implements CartRepository {
    private List<Cart> data = new ArrayList<>();
    private AtomicLong counter = new AtomicLong();
    @Override
    public Cart save(Cart cart) {
        if (cart.id() == null || cart.id() == 0L) {
            Cart newCart = new Cart(counter.incrementAndGet(),
                    cart.userId());
            data.add(newCart);
            return newCart;
        }else{
            data.removeIf(i -> i.id().equals(cart.id()));
            data.add(cart);
            return cart;
        }
    }

    @Override
    public Optional<Cart> findByUserId(String userId) {
        return data.stream().filter(cart -> cart.userId().equals(userId)).findFirst();
    }
}
