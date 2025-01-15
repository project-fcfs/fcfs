package hanghae.user_service.dummy;

import hanghae.user_service.domain.user.User;
import hanghae.user_service.infrastructure.user.UserEntity;
import hanghae.user_service.infrastructure.user.UserJpaRepository;
import hanghae.user_service.service.port.LocalDateTimeHolder;
import hanghae.user_service.service.port.PersonalDataEncryptor;
import hanghae.user_service.service.port.UUIDRandomHolder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class InitService0 {

    private static final Logger log = LoggerFactory.getLogger(InitService0.class);
    private final PersonalDataEncryptor encryptor;
    private final LocalDateTimeHolder localDateTimeHolder;
    private final UUIDRandomHolder uuidRandomHolder;
    private final UserJpaRepository jpaRepository;

    public InitService0(PersonalDataEncryptor encryptor, LocalDateTimeHolder localDateTimeHolder,
                        UUIDRandomHolder uuidRandomHolder, UserJpaRepository jpaRepository) {
        this.encryptor = encryptor;
        this.localDateTimeHolder = localDateTimeHolder;
        this.uuidRandomHolder = uuidRandomHolder;
        this.jpaRepository = jpaRepository;
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void dbInit1() {
        List<User> users = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 10_000; i++) {
            users.add(create("name" + i, "password" + i, "address" + i,
                    String.format("email%d@email.com", i)));

            // 배치 저장: 100개씩 저장
            if (users.size() == 100) {
                List<User> batchUsers = new ArrayList<>(users);
                // 배치 삽입을 멀티스레드로 처리 (각 쓰레드에서 트랜잭션을 관리)
                executorService.submit(() -> {
                    List<UserEntity> entities = batchUsers.stream().map(UserEntity::fromModel).toList();
                    jpaRepository.saveAll(entities);
                });
                users.clear();
            }
        }

        // 남은 데이터 저장
        if (!users.isEmpty()) {
            List<UserEntity> entities = users.stream().map(UserEntity::fromModel).toList();
            jpaRepository.saveAll(entities);
        }

        executorService.shutdown();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void dbInit2() {
        List<User> users = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            users.add(create("name" + i, "password" + i, "address" + i,
                    String.format("email%d@email.com", i)));
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
