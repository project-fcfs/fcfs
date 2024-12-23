package hanghae.order_service.service;

import hanghae.order_service.domain.cart.Cart;
import hanghae.order_service.domain.cart.CartProduct;
import hanghae.order_service.service.common.exception.CustomApiException;
import hanghae.order_service.service.common.util.ErrorMessage;
import hanghae.order_service.service.port.CartProductRepository;
import hanghae.order_service.service.port.CartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CartService {

    private final CartRepository cartRepository;
    private final CartProductRepository cartProductRepository;

    public CartService(CartRepository cartRepository, CartProductRepository cartProductRepository) {
        this.cartRepository = cartRepository;
        this.cartProductRepository = cartProductRepository;
    }

    @Transactional
    public void add(String userId, String productId) {
        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> cartRepository.save(Cart.create(userId)));
        cartProductRepository.save(CartProduct.create(productId, cart));
    }

    @Transactional
    public void updateQuantity(String userId, String productId, int count) {
        CartProduct cartProduct = getCartProduct(userId, productId);
        CartProduct updatedCartProduct = cartProduct.updateCount(count);
        cartProductRepository.save(updatedCartProduct);
    }

    @Transactional
    public void deleteProduct(String userId, String productId) {
        CartProduct cartProduct = getCartProduct(userId, productId);
        cartProductRepository.removeCartItem(cartProduct);
    }

    private CartProduct getCartProduct(String userId, String productId) {
        return cartProductRepository.findCartProduct(productId, userId)
                .orElseThrow(() -> new CustomApiException(ErrorMessage.NOT_FOUND_CART_PRODUCT.getMessage()));
    }
}