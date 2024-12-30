package hanghae.user_service.service.port;

public interface TokenStoreRepository {

    void save(String key, String value, Long expirationTime);
    void deleteToken(String key, String value);
}
