package hanghae.user_service.controller.req;

import hanghae.user_service.controller.valid.UserEmailUnique;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserCreateReqDto(
        @UserEmailUnique(message = "이메일은 공백이거나 중복일 수 없습니다")
        @Email(message = "올바른 이메일을 입력해주세요")
        String email,
        @Pattern(regexp = "^[가-힣\\w]{2,10}",message = "이름은 영문, 한글, 숫자 2~10자만 입력해주세요")
        String name,
        @NotBlank
        String password,
        @NotBlank
        String address,
        @Pattern(regexp = "^[\\d]{6,6}",message = "인증번호는 6자리입니다.")
        String authCode
) {
}
