package hanghae.user_service.service.port;

import hanghae.user_service.domain.address.Address;

public interface AddressRepository {

    void save(Address address);
}
