package hanghae.gateway_service.service.port;

import reactor.core.publisher.Mono;

public interface TokenStoreRepository {
    Mono<Boolean> existLoginToken(String key, String value);
}
