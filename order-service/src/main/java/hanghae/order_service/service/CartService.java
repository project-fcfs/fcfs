package hanghae.order_service.service;

import hanghae.order_service.domain.cart.Cart;
import hanghae.order_service.domain.cart.CartProduct;
import hanghae.order_service.domain.product.Product;
import hanghae.order_service.service.common.exception.CustomApiException;
import hanghae.order_service.service.common.exception.ErrorCode;
import hanghae.order_service.service.port.CartProductRepository;
import hanghae.order_service.service.port.CartRepository;
import hanghae.order_service.service.port.ProductClient;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CartService {

    private final CartRepository cartRepository;
    private final CartProductRepository cartProductRepository;
    private final ProductClient productClient;

    public CartService(CartRepository cartRepository, CartProductRepository cartProductRepository,
                       ProductClient productClient) {
        this.cartRepository = cartRepository;
        this.cartProductRepository = cartProductRepository;
        this.productClient = productClient;
    }

    /**
     * 장바구니에 담는다 Product-Service에서 올바른 상품인지 확인하고 장바구니에 담는다
     */
    @Transactional
    public void add(String userId, Long productId, int count) {
        Boolean validProduct = productClient.isValidProduct(productId);
        if (validProduct) {
            Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> cartRepository.save(Cart.create(userId)));
            cartProductRepository.save(CartProduct.create(productId, count, cart));
        } else {
            throw new CustomApiException(ErrorCode.INVALID_PRODUCT, String.valueOf(productId));
        }
    }

    /**
     * 장바구니 상품의 수량을 특정한 수량만큼 더하거나 뺄 수 있다 하지만 수량이 0이 될 순 없다
     */
    @Transactional
    public void updateQuantity(String userId, Long productId, int count) {
        CartProduct cartProduct = getCartProducts(userId, productId);
        CartProduct updatedCartProduct = cartProduct.updateCount(count);
        cartProductRepository.save(updatedCartProduct);
    }

    /**
     * 장바구니 속 상품을 지운다
     */
    @Transactional
    public void deleteProduct(String userId, List<Long> productIds) {
        cartProductRepository.removeCartItems(productIds, userId);
    }

    /**
     * 장바구니 속 상품을 가져온다
     */
    private CartProduct getCartProducts(String userId, Long productId) {
        return cartProductRepository.findCartProduct(productId, userId)
                .orElseThrow(() -> new CustomApiException(ErrorCode.NOT_FOUND_CART_PRODUCT, String.valueOf(productId)));
    }

    /**
     * 장바구니의 정보로 Product-service에서 정보를 가져와 유저에게 반환한다
     */
    public List<Product> getCartProducts(String userId) {
        List<CartProduct> cartProducts = cartProductRepository.findAllByUserId(userId);
        List<Long> productIds = cartProducts.stream()
                .map(CartProduct::productId)
                .toList();
        List<Product> products = productClient.getProducts(productIds);

        Map<Long, Product> map = products.stream()
                .collect(Collectors.toMap(Product::productId, v -> v));

        return cartProducts.stream()
                .map(i -> {
                    Product product = map.get(i.productId());
                    return product.convertCart(i.quantity());
                }).toList();


    }
}
