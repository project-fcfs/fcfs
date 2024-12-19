package hanghae.user_service.service;

import hanghae.user_service.domain.user.User;
import hanghae.user_service.service.common.exception.CustomApiException;
import hanghae.user_service.service.common.util.ErrorMessage;
import hanghae.user_service.service.port.LocalDateTimeHolder;
import hanghae.user_service.service.port.PersonalDataEncryptor;
import hanghae.user_service.service.port.UUIDRandomHolder;
import hanghae.user_service.service.port.UserRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UUIDRandomHolder uuidRandomHolder;
    private final LocalDateTimeHolder localDateTimeHolder;
    private final PersonalDataEncryptor personalDataEncryptor;

    public UserService(UserRepository userRepository, UUIDRandomHolder uuidRandomHolder,
                       LocalDateTimeHolder localDateTimeHolder, PersonalDataEncryptor personalDataEncryptor) {
        this.userRepository = userRepository;
        this.uuidRandomHolder = uuidRandomHolder;
        this.localDateTimeHolder = localDateTimeHolder;
        this.personalDataEncryptor = personalDataEncryptor;
    }

    @Transactional
    public void create(String name, String password, String address, String email) {
        String encodePassword = personalDataEncryptor.encodePassword(password);
        String encodedName = personalDataEncryptor.encodeData(name);
        String encodedEmail = personalDataEncryptor.encodeData(email);
        String encodedAddress = personalDataEncryptor.encodeData(address);

        User user = User.normalCreate(encodedName, encodePassword, encodedEmail, uuidRandomHolder.getRandomUUID(),
               encodedAddress, localDateTimeHolder.getCurrentDate());

        userRepository.save(user);
    }

    public void checkDuplicateEmail(String email) {
        String encodeEmail = personalDataEncryptor.encodeData(email);
        Optional<User> _user = userRepository.findByEmail(encodeEmail);
        if(_user.isPresent()) {
            throw new CustomApiException(ErrorMessage.DUPLICATE_EMAIL_ERROR.getMessage());
        }
    }

}
