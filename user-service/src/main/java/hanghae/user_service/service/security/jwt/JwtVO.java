package hanghae.user_service.service.security.jwt;

public class JwtVO {
    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String CATEGORY_ACCESS = "access_token";
    public static final String CATEGORY_REFRESH = "refresh_token";
    public static final String PAYLOAD_USER_ID = "user_id";

    public static final Long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 60L;
    public static final Long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 7L;
}
