package hanghae.order_service.infrastructure.cart;

import hanghae.order_service.domain.cart.CartProduct;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "cart_product")
public class CartProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private int count;
    @Column(nullable = false, unique = true)
    private String productId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private CartEntity cartEntity;

    public CartProductEntity(Long id, int count, String productId, CartEntity cartEntity) {
        this.id = id;
        this.count = count;
        this.productId = productId;
        this.cartEntity = cartEntity;
    }

    protected CartProductEntity() {
    }

    public static CartProductEntity fromModel(CartProduct cartProduct) {
        return new CartProductEntity(cartProduct.id(), cartProduct.quantity(),
                cartProduct.productId(), CartEntity.fromModel(cartProduct.cart()));
    }

    public CartProduct toModel() {
        return new CartProduct(id, count, productId, cartEntity.toModel());
    }
}
