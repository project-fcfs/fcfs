package hanghae.user_service.service;

import hanghae.user_service.domain.address.Address;
import hanghae.user_service.service.port.AddressRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AddressService {

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Transactional
    public void add(String baseAddress, String detailAddress, String extraAddress) {
        Address address = Address.create(baseAddress, detailAddress, extraAddress);
        addressRepository.save(address);
    }
}
