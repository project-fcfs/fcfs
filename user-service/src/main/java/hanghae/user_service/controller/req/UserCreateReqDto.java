package hanghae.user_service.controller.req;

public record UserCreateReqDto(
        String email,
        String name,
        String password,
        String baseAddress,
        String detailAddress,
        String extraAddress

) {
}
