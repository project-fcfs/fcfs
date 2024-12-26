package hanghae.gateway_service.util;

public enum ErrorMessage {

    INVALID_AUTHORIZATION_HEADER("No authorization header"),
    INVALID_JWT_TOKEN("Jwt token is not valid")
    ;

    private String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return "[ERROR] " + message;
    }
}
