package hanghae.user_service.service;

import hanghae.user_service.domain.user.User;
import hanghae.user_service.service.port.LocalDateTimeHolder;
import hanghae.user_service.service.port.UUIDRandomHolder;
import hanghae.user_service.service.port.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UUIDRandomHolder uuidRandomHolder;
    private final LocalDateTimeHolder localDateTimeHolder;

    public UserService(UserRepository userRepository, UUIDRandomHolder uuidRandomHolder,
                       LocalDateTimeHolder localDateTimeHolder) {
        this.userRepository = userRepository;
        this.uuidRandomHolder = uuidRandomHolder;
        this.localDateTimeHolder = localDateTimeHolder;
    }

    @Transactional
    public void create(String name, String password, String email) {
        User user = User.normalCreate(name, password, email, uuidRandomHolder.getRandomUUID(),
                localDateTimeHolder.getCurrentDate());

        userRepository.save(user);
    }
}
