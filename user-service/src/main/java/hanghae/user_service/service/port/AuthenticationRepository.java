package hanghae.user_service.service.port;

public interface AuthenticationRepository {

    void save(String key, String value, Long expiredMs);
    boolean existsCode(String key, String value);
}
