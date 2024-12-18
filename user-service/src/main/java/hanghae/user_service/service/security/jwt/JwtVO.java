package hanghae.user_service.service.security.jwt;

import org.springframework.beans.factory.annotation.Value;

public class JwtVO {
    public static final String HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String SUBJECT = "fcfs";
    public static final String CATEGORY_ACCESS = "access";
    public static final String CATEGORY_REFRESH = "refresh";
    public static final String CATEGORY_NAME = "category";
    public static final String PAYLOAD_EMAIL = "email";
    public static final String PAYLOAD_ROLE = "role";
    public static final String PAYLOAD_UUID = "uuid";

    public static final Long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 60L;
    public static final Long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 7L;
}
