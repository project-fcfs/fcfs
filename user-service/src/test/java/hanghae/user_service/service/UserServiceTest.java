package hanghae.user_service.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import hanghae.user_service.domain.user.User;
import hanghae.user_service.domain.user.UserRole;
import hanghae.user_service.mock.FakeLocalDateTimeHolder;
import hanghae.user_service.mock.FakePersonalDataEncryptor;
import hanghae.user_service.mock.FakeUUIDRandomHolder;
import hanghae.user_service.mock.FakeUserRepository;
import hanghae.user_service.service.port.LocalDateTimeHolder;
import hanghae.user_service.service.port.UUIDRandomHolder;
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

    @BeforeEach
    void setUp() {
        userRepository = new FakeUserRepository();
        encryptor = new FakePersonalDataEncryptor();
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
        String uuid = "random2";
        uuidRandomHolder.setValue(uuid);

        // when
        userService.create(name,password,email);
        User result = userRepository.findById(1L).get();

        // then
        assertAll(() -> {
            assertThat(result.name()).isEqualTo(encryptor.decodeData(result.name()));
            assertThat(encryptor.matchesPassword(password, result.password())).isTrue();
            assertThat(result.email()).isEqualTo(encryptor.decodeData(result.email()));
            assertThat(result.role()).isEqualByComparingTo(UserRole.ROLE_USER);
            assertThat(result.UUID()).isEqualTo(uuid);
            assertThat(result.createdAt()).isEqualTo(localDateTime);
        });
    }
}