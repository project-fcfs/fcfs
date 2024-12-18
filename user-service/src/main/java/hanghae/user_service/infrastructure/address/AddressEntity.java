package hanghae.user_service.infrastructure.address;

import hanghae.user_service.domain.address.Address;
import hanghae.user_service.domain.address.DefaultAddressStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class AddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    String baseAddress;
    @Column(nullable = false)
    String detailAddress;
    String extraAddress;
    @Enumerated(EnumType.STRING)
    DefaultAddressStatus defaultAddressStatus;

    public AddressEntity(Long id, String baseAddress, String detailAddress, String extraAddress,
                         DefaultAddressStatus defaultAddressStatus) {
        this.id = id;
        this.baseAddress = baseAddress;
        this.detailAddress = detailAddress;
        this.extraAddress = extraAddress;
        this.defaultAddressStatus = defaultAddressStatus;
    }

    protected AddressEntity() {
    }

    public static AddressEntity fromModel(Address address) {
        return new AddressEntity(address.id(), address.baseAddress(),address.detailAddress(),
                address.extraAddress(),address.defaultAddressStatus());
    }

    public Address toModel(){
        return new Address(id, baseAddress, detailAddress, extraAddress, defaultAddressStatus);
    }
}
