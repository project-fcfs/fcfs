package hanghae.user_service.controller;

import hanghae.user_service.controller.req.UserAuthCodeReqDto;
import hanghae.user_service.controller.req.UserCreateReqDto;
import hanghae.user_service.controller.resp.ResponseDto;
import hanghae.user_service.controller.resp.UserCreateRespDto;
import hanghae.user_service.controller.resp.UserInfoRespDto;
import hanghae.user_service.domain.user.User;
import hanghae.user_service.service.AuthenticationService;
import hanghae.user_service.service.UserFacade;
import hanghae.user_service.service.UserService;
import hanghae.user_service.service.security.model.LoginUser;
import hanghae.user_service.service.security.model.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User Controller", description = "일반 사용자 서비스를 위한 API 컨트롤러입니다.")
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserFacade userFacade;
    private final AuthenticationService authenticationService;
    private final UserService userService;

    public UserController(UserFacade userFacade, AuthenticationService authenticationService, UserService userService) {
        this.userFacade = userFacade;
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @Operation(summary = "회원가입 API", description = "회원 가입을 처리하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 가입 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    })
    @PostMapping("/signup")
    public ResponseDto<?> signUp(@Valid @RequestBody UserCreateReqDto reqDto) {
        User user = userFacade.register(reqDto);
        UserCreateRespDto response = UserCreateRespDto.of(user);
        return new ResponseDto<>(1, "success", response);
    }

    @Operation(
            summary = "인증 코드 전송 API",
            description = "사용자 이메일로 인증 코드를 전송합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "인증 코드 전송 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
            }
    )
    @PostMapping("/authcode")
    public ResponseEntity<?> sendAuthCode(@Valid @RequestBody UserAuthCodeReqDto reqDto) {
        authenticationService.send(reqDto.email());
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "마이페이지 조회 API",
            description = "로그인한 사용자의 마이페이지 정보를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "401", description = "인증 실패")
            }
    )
    @GetMapping("/user/mypage")
    public ResponseEntity<?> getMyPage(@AuthenticationPrincipal PrincipalDetails principal) {
        LoginUser loginUser = principal.getLoginUser();
        User user = userService.getUser(loginUser.id());
        return new ResponseEntity<>(UserInfoRespDto.of(user), HttpStatus.OK);
    }

    @Operation(
            summary = "사용자 목록 조회 API",
            description = "시스템에 등록된 모든 사용자의 목록을 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "조회 성공",
                            content = @Content(schema = @Schema(implementation = User.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "접근 권한 없음")
            }
    )
    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        List<User> users = userService.getUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
