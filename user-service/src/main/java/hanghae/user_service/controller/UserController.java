package hanghae.user_service.controller;

import hanghae.user_service.controller.req.UserAuthCodeReqDto;
import hanghae.user_service.controller.req.UserCreateReqDto;
import hanghae.user_service.controller.resp.UserCreateRespDto;
import hanghae.user_service.controller.resp.UserInfoRespDto;
import hanghae.user_service.domain.user.User;
import hanghae.user_service.service.AuthenticationService;
import hanghae.user_service.service.UserFacade;
import hanghae.user_service.service.UserService;
import hanghae.user_service.service.security.model.PrincipalDetails;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserFacade userFacade;
    private final AuthenticationService authenticationService;
    private final UserService userService;

    public UserController(UserFacade userFacade, AuthenticationService authenticationService, UserService userService) {
        this.userFacade = userFacade;
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody UserCreateReqDto reqDto) {
        User user = userFacade.register(reqDto);
        return new ResponseEntity<>(UserCreateRespDto.of(user), HttpStatus.CREATED);
    }

    @PostMapping("/authcode")
    public ResponseEntity<?> sendAuthCode(@Valid @RequestBody UserAuthCodeReqDto reqDto){
        authenticationService.send(reqDto.email());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/mypage")
    public ResponseEntity<?> getMyPage(@AuthenticationPrincipal PrincipalDetails principal){
        User user = userService.getUser(principal.getUsername());
        return new ResponseEntity<>(UserInfoRespDto.of(user), HttpStatus.OK);
    }
}
