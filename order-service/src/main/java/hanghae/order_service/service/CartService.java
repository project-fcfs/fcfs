package hanghae.order_service.service;

import hanghae.order_service.controller.resp.CartProductRespDto;
import hanghae.order_service.controller.resp.ResponseDto;
import hanghae.order_service.domain.cart.Cart;
import hanghae.order_service.domain.cart.CartProduct;
import hanghae.order_service.service.common.exception.CustomApiException;
import hanghae.order_service.service.common.util.ErrorMessage;
import hanghae.order_service.service.port.ProductClient;
import hanghae.order_service.service.port.CartProductRepository;
import hanghae.order_service.service.port.CartRepository;
import java.util.List;
import org.springframework.http.ResponseEntity;
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
     * 장바구니에 담는다
     * 장바구니에 담을 때는 상품ID만 담고 수량을 1로 체크한다
     */
    @Transactional
    public void add(String userId, String productId) {
        ResponseEntity<ResponseDto<?>> product = productClient.isValidProduct(productId);
        if(product.getStatusCode().is2xxSuccessful()){
            Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> cartRepository.save(Cart.create(userId)));
            cartProductRepository.save(CartProduct.create(productId, cart));
        } else{
            throw new CustomApiException(ErrorMessage.INVALID_PRODUCT.getMessage());
        }
    }

    /**
     * 장바구니 상품의 수량을 특정한 수량만큼 더하거나 뺄 수 있다
     * 하지만 수량이 0이 될 순 없다
     */
    @Transactional
    public void updateQuantity(String userId, String productId, int count) {
        CartProduct cartProduct = getCartProducts(userId, productId);
        CartProduct updatedCartProduct = cartProduct.updateCount(count);
        cartProductRepository.save(updatedCartProduct);
    }

    /**
     * 장바구니 속 상품을 지운다
     */
    @Transactional
    public void deleteProduct(String userId, String productId) {
        CartProduct cartProduct = getCartProducts(userId, productId);
        cartProductRepository.removeCartItem(cartProduct);
    }

    /**
     * 장바구니 속 상품을 가져온다
     */
    private CartProduct getCartProducts(String userId, String productId) {
        return cartProductRepository.findCartProduct(productId, userId)
                .orElseThrow(() -> new CustomApiException(ErrorMessage.NOT_FOUND_CART_PRODUCT.getMessage()));
    }

    /**
     * 장바구니의 정보로 Product-service에서 정보를 가져와 유저에게 반환한다
     */
    public List<CartProductRespDto> getCartProducts(String userId) {
        List<CartProduct> cartProducts = cartProductRepository.findAllByUserId(userId);
        // todo Product-service에서 상품정보 가져와서 반환하기
        return null;
    }
}
