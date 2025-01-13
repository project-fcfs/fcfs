package hanghae.user_service.controller.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "사용자 로그인 요청 DTO")
public record UserLoginReqDto(

        @Schema(description = "사용자의 이메일 주소", example = "user@example.com", required = true)
        @Email(message = "올바른 이메일을 입력해주세요")
        @NotBlank(message = "이메일은 필수값입니다.")
        String email,

        @Schema(description = "사용자의 비밀번호", example = "password123", required = true)
        @NotBlank(message = "비밀번호는 필수값입니다.")
        String password
) {
}
