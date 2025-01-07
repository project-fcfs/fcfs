package hanghae.order_service.controller;

import hanghae.order_service.controller.req.CartCreateReqDto;
import hanghae.order_service.controller.req.CartDeleteReqDto;
import hanghae.order_service.controller.req.CartUpdateReqDto;
import hanghae.order_service.controller.resp.ProductRespDto;
import hanghae.order_service.controller.resp.ResponseDto;
import hanghae.order_service.domain.product.Product;
import hanghae.order_service.service.CartService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<?> addCartProduct(@RequestHeader("userId") String userId,
                                            @RequestBody CartCreateReqDto reqDto) {
        cartService.add(userId, reqDto.productId(), reqDto.count());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCartProduct(@RequestHeader("userId") String userId,
                                               @RequestBody List<CartDeleteReqDto> reqDto) {
        List<Long> productIds = reqDto.stream().map(CartDeleteReqDto::productId).toList();
        cartService.deleteProduct(userId, productIds);
        return ResponseEntity.ok().build();
    }

    @PatchMapping()
    public ResponseEntity<?> updateCartProduct(@RequestHeader("userId") String userId,
                                               @RequestBody CartUpdateReqDto reqDto) {
        cartService.updateQuantity(userId, reqDto.productId(), reqDto.count());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/products")
    public ResponseDto<?> getCartProduct(@RequestHeader("userId") String userId) {
        List<Product> products = cartService.getCartProducts(userId);
        List<ProductRespDto> response = products.stream().map(ProductRespDto::of)
                .toList();

        return ResponseDto.success(response, HttpStatus.OK);
    }
}
