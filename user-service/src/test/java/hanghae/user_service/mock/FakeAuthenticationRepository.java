package hanghae.user_service.mock;

import hanghae.user_service.service.port.AuthenticationRepository;
import java.util.HashMap;
import java.util.Map;

public class FakeAuthenticationRepository implements AuthenticationRepository {
    private Map<String, String> data = new HashMap<>();

    @Override
    public void save(String key, String value, Long expiredMs) {
        data.put(key, value);
    }

    @Override
    public boolean existsCode(String key, String value) {
        String input = data.get(key);
        return input != null && input.equals(value);
    }
}
