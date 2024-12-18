package hanghae.user_service.service.security;

import hanghae.user_service.domain.user.User;
import hanghae.user_service.service.port.PersonalDataEncryptor;
import hanghae.user_service.service.port.UserRepository;
import hanghae.user_service.service.security.model.LoginUser;
import hanghae.user_service.service.security.model.PrincipalDetails;
import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class FormUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PersonalDataEncryptor personalDataEncryptor;

    public FormUserDetailService(UserRepository userRepository, PersonalDataEncryptor personalDataEncryptor) {
        this.userRepository = userRepository;
        this.personalDataEncryptor = personalDataEncryptor;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> _user = userRepository.findByEmail(username);
        if (_user.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        LoginUser loginUser = LoginUser.create(_user.get());
        return new PrincipalDetails(loginUser);
    }
}
