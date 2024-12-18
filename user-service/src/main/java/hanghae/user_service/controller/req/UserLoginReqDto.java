package hanghae.user_service.controller.req;

public record UserLoginReqDto(
        String email,
        String password
) {
}
