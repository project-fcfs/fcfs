package hanghae.user_service.controller;

import hanghae.user_service.controller.req.UserAuthCodeReqDto;
import hanghae.user_service.controller.req.UserCreateReqDto;
import hanghae.user_service.service.AuthenticationService;
import hanghae.user_service.service.UserFacade;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserFacade userFacade;
    private final AuthenticationService authenticationService;

    public UserController(UserFacade userFacade, AuthenticationService authenticationService) {
        this.userFacade = userFacade;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody UserCreateReqDto reqDto) {
        userFacade.register(reqDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/authcode")
    public ResponseEntity<?> sendAuthCode(@Valid @RequestBody UserAuthCodeReqDto reqDto){
        authenticationService.send(reqDto.email());
        return ResponseEntity.ok().build();
    }
}
