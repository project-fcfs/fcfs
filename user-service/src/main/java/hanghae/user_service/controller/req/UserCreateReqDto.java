package hanghae.user_service.controller.req;

public record UserCreateReqDto(
        String email,
        String name,
        String password,
        String address,
        String authCode
) {
}
