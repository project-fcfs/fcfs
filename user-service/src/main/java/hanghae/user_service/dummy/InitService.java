package hanghae.user_service.dummy;

import hanghae.user_service.domain.user.User;
import hanghae.user_service.infrastructure.user.UserEntity;
import hanghae.user_service.infrastructure.user.UserJpaRepository;
import hanghae.user_service.service.port.LocalDateTimeHolder;
import hanghae.user_service.service.port.PersonalDataEncryptor;
import hanghae.user_service.service.port.UUIDRandomHolder;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class InitService {

    private final PersonalDataEncryptor encryptor;
    private final LocalDateTimeHolder localDateTimeHolder;
    private final UUIDRandomHolder uuidRandomHolder;
    private final UserJpaRepository jpaRepository;
    private final EntityManager em;

    public InitService(PersonalDataEncryptor encryptor, LocalDateTimeHolder localDateTimeHolder,
                       UUIDRandomHolder uuidRandomHolder, UserJpaRepository jpaRepository, EntityManager em) {
        this.encryptor = encryptor;
        this.localDateTimeHolder = localDateTimeHolder;
        this.uuidRandomHolder = uuidRandomHolder;
        this.jpaRepository = jpaRepository;
        this.em = em;
    }

    @Transactional
    public void dbInit() {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            users.add(create("name" + i, "password" + i, "address" + i,
                    String.format("email%d@email.com", i)));

            // 배치 저장: 1000개씩 저장
            if (users.size() == 100) {
                List<UserEntity> entities = users.stream().map(UserEntity::fromModel).toList();
                jpaRepository.saveAll(entities);
                jpaRepository.flush();
                em.clear();
                users.clear();
            }
        }

        // 남은 데이터 저장
        if (!users.isEmpty()) {
            List<UserEntity> entities = users.stream().map(UserEntity::fromModel).toList();
            jpaRepository.saveAll(entities);
        }
    }

    public User create(String name, String password, String address, String email) {
        String encodePassword = encryptor.encodePassword(password);
        String encodedName = encryptor.encodeData(name);
        String encodedEmail = encryptor.encodeData(email);
        String encodedAddress = encryptor.encodeData(address);

        return User.normalCreate(encodedName, encodePassword, encodedEmail, uuidRandomHolder.getRandomUUID(),
                encodedAddress, localDateTimeHolder.getCurrentDate());
    }
}
