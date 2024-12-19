package hanghae.user_service.service.common.util;

public enum ErrorMessage {

    NOT_FOUND_USER_ROLE("유효하지 않는 유저 권한입니다."),
    NOT_FOUND_USER("요청하신 유저를 찾을 수 없습니다."),
    NOT_FOUND_ADDRESS("요청하신 주소를 찾을 수 없습니다."),
    ERROR_ACCESS_DENIED("권한이 없습니다."),
    MUST_LOGIN_REQUIRED("로그인을 진행해 주세요"),
    INVALID_JWT_TOKEN("유효하지 않은 토큰입니다."),
    INVALID_DATA_BINDING("유효하지 않은 바인딩입니다.")

    ;
    private String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return "[ERROR] " + message;
    }
}
