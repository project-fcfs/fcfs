package hanghae.user_service.infrastructure.user;

import hanghae.user_service.domain.user.User;
import hanghae.user_service.domain.user.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false, unique = true)
    private String userId;
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;

    protected UserEntity() {
    }

    public UserEntity(Long id, String name, String password, String email, UserRole role, String address, String userId,
                      LocalDateTime createdAt, LocalDateTime deletedAt) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;
        this.address = address;
        this.userId = userId;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
    }

    public static UserEntity fromModel(User user) {
        return new UserEntity(user.id(), user.name(), user.password(), user.email(), user.role(),
                user.address(), user.userId(), user.createdAt(), user.deletedAt());
    }

    public User toModel() {
        return new User(id, name, password, email, role, userId, address, createdAt, deletedAt);
    }
}
