package hanghae.product_service.domain.product;

public enum ProductStatus {
    ACTIVE("재고 있음"), SOLD_OUT("품절"), INACTIVE("비활성화");

    private final String description;

    ProductStatus(String description) {
        this.description = description;
    }


}
