package hanghae.user_service.infrastructure.address;

import hanghae.user_service.domain.address.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressJpaRepository extends JpaRepository<AddressEntity, Long> {
}
