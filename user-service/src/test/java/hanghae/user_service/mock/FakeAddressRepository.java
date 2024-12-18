package hanghae.user_service.mock;

import hanghae.user_service.domain.address.Address;
import hanghae.user_service.service.port.AddressRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class FakeAddressRepository implements AddressRepository {
    private List<Address> data = new ArrayList<>();
    private AtomicLong counter = new AtomicLong();

    @Override
    public void save(Address address) {
        if (address.id() == null || address.id() == 0L) {
            Address newUser = new Address(
                    counter.incrementAndGet(),
                    address.baseAddress(),
                    address.detailAddress(),
                    address.extraAddress(),
                    address.defaultAddressStatus());

            data.add(newUser);
        } else {
            data.removeIf(i -> Objects.equals(i.id(), address.id()));
            data.add(address);
        }

    }

}
