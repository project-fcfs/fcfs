package hanghae.order_service.controller;

import hanghae.order_service.controller.resp.CartProductRespDto;
import hanghae.order_service.domain.cart.CartProduct;
import hanghae.order_service.service.CartService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/{productId}")
    public ResponseEntity<?> addCartProduct(@PathVariable("productId") String productId,
                                         @RequestHeader("userId") String userId) {
        cartService.add(userId,productId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteCartProduct(@PathVariable("productId") String productId,
                                            @RequestHeader("userId") String userId) {
        cartService.deleteProduct(userId,productId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<?> updateCartProduct(@PathVariable("productId") String productId,
                                               @RequestHeader("userId") String userId, @RequestParam("count") int count) {
        cartService.updateQuantity(userId, productId, count);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/products")
    public ResponseEntity<?> getCartProduct(@RequestHeader("userId") String userId) {
        List<CartProductRespDto> cartProducts = cartService.getCartProducts(userId);

        return new ResponseEntity<>(cartProducts, HttpStatus.OK);
    }
}
