package hanghae.user_service.infrastructure.address;

import hanghae.user_service.domain.address.Address;
import hanghae.user_service.service.port.AddressRepository;
import org.springframework.stereotype.Repository;

@Repository
public class AddressRepositoryImpl implements AddressRepository {
    private final AddressJpaRepository jpaRepository;

    public AddressRepositoryImpl(AddressJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(Address address) {
        jpaRepository.save(AddressEntity.fromModel(address));
    }
}
