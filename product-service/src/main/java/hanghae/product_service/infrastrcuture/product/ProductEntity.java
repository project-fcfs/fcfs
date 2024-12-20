package hanghae.product_service.infrastrcuture.product;

import hanghae.product_service.domain.product.Product;
import hanghae.product_service.domain.product.ProductStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "product")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private String UUID;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus;

    protected ProductEntity() {
    }

    public ProductEntity(Long id, String name, int price, int quantity, String UUID,
                         ProductStatus productStatus) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.UUID = UUID;
        this.productStatus = productStatus;
    }

    public static ProductEntity fromModel(Product product) {
        return new ProductEntity(product.id(), product.name(), product.price(),
                product.quantity(), product.UUID(), product.ProductStatus());
    }

    public Product toModel() {
        return new Product(id, name, price, quantity, UUID, productStatus);
    }
}
