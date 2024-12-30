package hanghae.gateway_service.service.port;

public interface TokenStoreRepository {
    boolean existLoginToken(String key, String value);
}
