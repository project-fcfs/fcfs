package hanghae.user_service.domain.address;

public record Address(
        Long id,
        String baseAddress,
        String detailAddress,
        String extraAddress,
        DefaultAddressStatus defaultAddressStatus
) {

    public static Address create(String baseAddress, String detailAddress, String extraAddress) {
        return new Address(null, baseAddress, detailAddress, extraAddress, DefaultAddressStatus.PRIMARY);
    }
}
