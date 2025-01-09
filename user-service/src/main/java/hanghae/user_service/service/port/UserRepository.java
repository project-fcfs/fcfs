package hanghae.user_service.service.port;

import hanghae.user_service.domain.user.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findByEmail(String email);

    Optional<User> findByUserId(String userId);

    Optional<User> findById(Long id);

    List<User> findAll();
}
