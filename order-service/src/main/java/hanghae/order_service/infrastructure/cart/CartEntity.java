package hanghae.order_service.infrastructure.cart;

import hanghae.order_service.domain.cart.Cart;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class CartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userId;

    public CartEntity(Long id, String userId) {
        this.id = id;
        this.userId = userId;
    }

    protected CartEntity() {
    }

    public static CartEntity fromModel(Cart cart) {
        return new CartEntity(cart.id(), cart.userId());
    }

    public Cart toModel(){
        return new Cart(id, userId);
    }
}
