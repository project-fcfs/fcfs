package hanghae.product_service.domain.imagefile;

public enum ImageFileStatus {
    ACTIVE("활성화"), INACTIVE("비활성화");

    private final String description;

    ImageFileStatus(String description) {
        this.description = description;
    }
}
