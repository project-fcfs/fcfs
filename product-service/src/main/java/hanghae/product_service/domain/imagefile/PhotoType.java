package hanghae.product_service.domain.imagefile;

public enum PhotoType {
    THUMBNAIL("썸네일 이미지"), PRODUCT("상품 이미지");

    private String description;

    PhotoType(String description) {
        this.description = description;
    }
}
