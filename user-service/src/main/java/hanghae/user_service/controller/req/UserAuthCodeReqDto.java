package hanghae.user_service.controller.req;

import hanghae.user_service.controller.valid.UserEmailUnique;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;

@Schema(description = "사용자 인증 코드를 요청하기 위한 DTO")
public record UserAuthCodeReqDto(

        @Schema(description = "사용자의 이메일 주소", example = "user@example.com", required = true)
        @Email(message = "올바른 이메일을 입력해주세요")
        @UserEmailUnique(message = "이메일은 공백이거나 중복일 수 없습니다")
        String email
) {
}
