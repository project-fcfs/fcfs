package hanghae.product_service.domain.product;

public enum ProductType {

    BASIC("일반 상품"), LIMITED("선착순 상품");

    private final String description;

    ProductType(String description) {
        this.description = description;
    }

}
