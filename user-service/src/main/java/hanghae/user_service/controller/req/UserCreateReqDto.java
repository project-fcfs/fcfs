package hanghae.user_service.controller.req;

import hanghae.user_service.controller.valid.UserEmailUnique;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "사용자 회원가입을 위한 요청 DTO")
public record UserCreateReqDto(

        @Schema(description = "사용자의 이메일 주소", example = "user@example.com", required = true)
        @UserEmailUnique(message = "이메일은 공백이거나 중복일 수 없습니다")
        @Email(message = "올바른 이메일을 입력해주세요")
        String email,

        @Schema(description = "사용자의 이름 (영문, 한글, 숫자 2~10자)", example = "홍길동", required = true)
        @Pattern(regexp = "^[가-힣\\w]{2,10}", message = "이름은 영문, 한글, 숫자 2~10자만 입력해주세요")
        String name,

        @Schema(description = "사용자의 비밀번호", example = "password123", required = true)
        @NotBlank
        String password,

        @Schema(description = "사용자의 주소", example = "서울특별시 강남구 테헤란로 123", required = true)
        @NotBlank
        String address,

        @Schema(description = "인증 코드 (6자리 숫자)", example = "123456", required = true)
        @Pattern(regexp = "\\d{6}", message = "인증 코드는 6자리만 입력해주세요")
        String authCode
) {
}
