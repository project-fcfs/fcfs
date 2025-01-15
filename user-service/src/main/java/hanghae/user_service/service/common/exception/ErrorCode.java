package hanghae.user_service.service.common.exception;

public enum ErrorCode {

    // 공통 에러
    INVALID_REQUEST(1000, "올바르지 않은 요청입니다."),
    ERROR_PARSE_DATA(1001, "문자열 변환 중에 문제가 발생했습니다."),
    INVALID_DATA_BINDING(1002, "유효하지 않은 바인딩입니다."),
    INTERNAL_SERVER_ERROR(1003, "서버 에러가 발생하였습니다. 관리자에게 문의해 주세요."),

    // 상품 에러
    INVALID_USER_ROLE(5001, "유효하지 않는 유저 권한입니다"),
    NOT_FOUND_USER_ADDRESS(5002, "요청하신 주소를 찾을 수 없습니다."),
    NOT_FOUND_USER_ID(5003, "요청하신 유저를 찾을 수 없습니다."),
    ERROR_ACCESS_DENIED(5004, "권한이 없습니다."),
    MUST_LOGIN_REQUIRED(5005, "로그인을 진행해 주세요."),
    INVALID_JWT_TOKEN(5006, "유효하지 않은 토큰입니다."),
    INVALID_AUTH_TOKEN(5007, "유효하지 않은 인증번호입니다."),
    DUPLICATE_USER_EMAIL(5008, "중복된 이메일입니다."),
    LOGIN_FAIL(5009, "로그인에 실패했습니다.");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
