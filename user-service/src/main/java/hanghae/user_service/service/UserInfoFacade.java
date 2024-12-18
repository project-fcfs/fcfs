package hanghae.user_service.service;

import org.springframework.stereotype.Service;

@Service
public class UserInfoFacade {

    private final AddressService addressService;
    private final UserService userService;

    public UserInfoFacade(AddressService addressService, UserService userService) {
        this.addressService = addressService;
        this.userService = userService;
    }
}
