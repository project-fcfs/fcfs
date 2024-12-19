package hanghae.user_service.controller.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserLoginReqDto(
        @Email(message = "올바른 이메일을 입력해주세요")
        @NotBlank(message = "이메일은 필수값입니다.")
        String email,
        @NotBlank
        String password
) {
}
