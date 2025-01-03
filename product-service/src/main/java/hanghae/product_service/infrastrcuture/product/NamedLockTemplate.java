package hanghae.product_service.infrastrcuture.product;

import static java.util.Objects.isNull;

import java.util.Map;
import java.util.function.Supplier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class NamedLockTemplate {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static final String GET_LOCK = "SELECT GET_LOCK(:userLockName, :timeoutSeconds)";
    private static final String RELEASE_LOCK = "SELECT RELEASE_LOCK(:userLockName)";

    public NamedLockTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public <T> T executeWithLock(String userLockName, int timeoutSeconds, Supplier<T> supplier) {
        try {
            getLock(userLockName, timeoutSeconds);
            return supplier.get();
        } finally {
            releaseLock(userLockName);
        }
    }

    private void getLock(String userLockName, int timeoutSeconds) {
        Map<String, Object> params = Map.of(
                "userLockName", userLockName,
                "timeoutSeconds", timeoutSeconds
        );

        Integer result = namedParameterJdbcTemplate.queryForObject(GET_LOCK, params, Integer.class);
        checkResult(result);
    }

    private void releaseLock(String userLockName) {
        Map<String, Object> params = Map.of("userLockName", userLockName);

        Integer result = namedParameterJdbcTemplate.queryForObject(RELEASE_LOCK, params, Integer.class);
        checkResult(result);
    }

    private void checkResult(Integer result) {
        if (isNull(result) || result != 1) {
            throw new RuntimeException("locked.resource");
        }
    }
}
