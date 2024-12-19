package hanghae.user_service.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import hanghae.user_service.domain.user.User;
import hanghae.user_service.domain.user.UserRole;
import hanghae.user_service.mock.FakeLocalDateTimeHolder;
import hanghae.user_service.mock.FakePersonalDataEncryptor;
import hanghae.user_service.mock.FakeUUIDRandomHolder;
import hanghae.user_service.mock.FakeUserRepository;
import hanghae.user_service.service.common.exception.CustomApiException;
import hanghae.user_service.service.common.util.ErrorMessage;
import hanghae.user_service.service.port.LocalDateTimeHolder;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserServiceTest {

    private UserService userService;
    private FakeUserRepository userRepository;
    private FakePersonalDataEncryptor encryptor;
    private FakeUUIDRandomHolder uuidRandomHolder;
    private LocalDateTime localDateTime = LocalDateTime.of(2024, 12, 18, 13, 34, 56);
    private String encrypt = "fake_";

    @BeforeEach
    void setUp() {
        userRepository = new FakeUserRepository();
        encryptor = new FakePersonalDataEncryptor(encrypt);
        uuidRandomHolder = new FakeUUIDRandomHolder("random");
        LocalDateTimeHolder localDateTimeHolder = new FakeLocalDateTimeHolder(localDateTime);
        userService = new UserService(userRepository, uuidRandomHolder, localDateTimeHolder, encryptor);
    }

    @Test
    @DisplayName("올바른 값을 입력하면 유저를 저장할 수 있다")
    void createUser() throws Exception {
        // given
        String name = "홍길동";
        String password = "password";
        String email = "email@email.com";
        String address = "address";
        String uuid = "random2";
        uuidRandomHolder.setValue(uuid);

        // when
        userService.create(name, password, address, email);
        User result = userRepository.findById(1L).get();

        // then
        assertAll(() -> {
            assertThat(result.name()).isEqualTo(encrypt + name);
            assertThat(encryptor.matchesPassword(password, result.password())).isTrue();
            assertThat(result.email()).isEqualTo(encrypt + email);
            assertThat(result.role()).isEqualByComparingTo(UserRole.ROLE_USER);
            assertThat(result.UUID()).isEqualTo(uuid);
            assertThat(result.email()).isEqualTo(encrypt + email);
            assertThat(result.createdAt()).isEqualTo(localDateTime);
        });
    }

    @Test
    @DisplayName("이미 저장되어 있는 이메일이 있다면 에러를 반환한다")
    void duplicateEmailError() throws Exception {
        // given
        String email = "email@email.com";
        userService.create("name", "password", "address", email);

        // then
        assertThatThrownBy(() -> userService.checkDuplicateEmail(email))
                .isInstanceOf(CustomApiException.class)
                .hasMessage(ErrorMessage.DUPLICATE_EMAIL_ERROR.getMessage());
    }
}