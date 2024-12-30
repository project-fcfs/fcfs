package hanghae.user_service.service.port;

public interface TokenStoreRepository {

    void save(String key, Integer value, Long expirationTime);
    void deleteToken(String key);
}
