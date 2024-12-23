package hanghae.user_service.service;

import hanghae.user_service.controller.req.UserCreateReqDto;
import hanghae.user_service.domain.user.User;
import org.springframework.stereotype.Service;

@Service
public class UserFacade {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    public UserFacade(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    public User register(UserCreateReqDto reqDto) {
        authenticationService.verifyCode(reqDto.email(), reqDto.authCode());
        return userService.create(reqDto.name(), reqDto.password(), reqDto.address(), reqDto.email());
    }
}
