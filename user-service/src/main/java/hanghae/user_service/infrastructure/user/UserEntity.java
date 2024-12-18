package hanghae.user_service.infrastructure.user;

import hanghae.user_service.domain.user.User;
import hanghae.user_service.domain.user.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    String name;
    @Column(nullable = false)
    String password;
    @Column(nullable = false)
    String email;
    @Column(nullable = false)
    UserRole role;
    @Column(nullable = false)
    String UUID;
    @Column(updatable = false, nullable = false)
    LocalDateTime createdAt;
    @Column(nullable = false)
    LocalDateTime updatedAt;

    protected UserEntity() {
    }

    public UserEntity(Long id, String name, String password, String email, UserRole role, String UUID, LocalDateTime createdAt,
                      LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;
        this.UUID = UUID;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static UserEntity fromModel(User user) {
        return new UserEntity(user.id(), user.name(), user.password(), user.email(), user.role(),
                user.UUID(), user.createdAt(), user.updatedAt());
    }

    public User toModel(){
        return new User(id, name, password, email, role, UUID, createdAt, updatedAt);
    }
}
