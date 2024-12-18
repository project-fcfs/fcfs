package hanghae.user_service.service.port;

import hanghae.user_service.domain.user.User;
import java.util.Optional;

public interface UserRepository {

    void save(User user);
    Optional<User> findByEmail(String email);
}
