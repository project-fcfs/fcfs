package hanghae.user_service.service;

import hanghae.user_service.controller.req.UserCreateReqDto;
import org.springframework.stereotype.Service;

@Service
public class UserFacade {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    public UserFacade(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    public void register(UserCreateReqDto reqDto) {
        authenticationService.verifyCode(reqDto.email(), reqDto.authCode());
        userService.create(reqDto.name(), reqDto.password(), reqDto.address(), reqDto.email());
    }
}
