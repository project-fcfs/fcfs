package hanghae.user_service.domain.address;

public enum DefaultAddressStatus {
    PRIMARY("기본 배송지"), SECONDARY("추가 배송지");

    private final String description;

    DefaultAddressStatus(String description) {
        this.description = description;
    }
}
