package hanghae.user_service.controller.req;

import hanghae.user_service.controller.valid.UserEmailUnique;
import jakarta.validation.constraints.Email;

public record UserAuthCodeReqDto(
        @Email(message = "올바른 이메일을 입력해주세요")
        @UserEmailUnique(message = "이메일은 공백이거나 중복일 수 없습니다")
        String email
) {
}
