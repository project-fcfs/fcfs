package hanghae.user_service.mock;

import hanghae.user_service.domain.user.User;
import hanghae.user_service.service.port.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class FakeUserRepository implements UserRepository {
    private List<User> data = new ArrayList<>();
    private AtomicLong counter = new AtomicLong();

    @Override
    public User save(User user) {
        if (user.id() == null || user.id() == 0L) {
            User newUser = new User(
                    counter.incrementAndGet(),
                    user.name(),
                    user.password(),
                    user.email(),
                    user.role(),
                    user.userId(),
                    user.address(),
                    user.createdAt(),
                    user.deletedAt());
            data.add(newUser);
            return newUser;
        }else{
            data.removeIf(i -> Objects.equals(i.id(), user.id()));
            data.add(user);
            return user;
        }

    }

    @Override
    public Optional<User> findByEmail(String email) {
        return data.stream().filter(i -> i.email().equals(email)).findFirst();
    }

    public Optional<User> findById(Long id) {
        return data.stream().filter(i -> i.id().equals(id)).findFirst();
    }

    @Override
    public Optional<User> findByUserId(String userId) {
        return data.stream().filter(i -> i.userId().equals(userId)).findFirst();
    }
}
