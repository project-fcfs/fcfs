package hanghae.user_service.infrastructure.user;

import hanghae.user_service.domain.user.User;
import hanghae.user_service.domain.user.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private UserRole role;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private String UUID;
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    protected UserEntity() {
    }

    public UserEntity(Long id, String name, String password, String email, UserRole role, String address, String UUID,
                      LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;
        this.address = address;
        this.UUID = UUID;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static UserEntity fromModel(User user) {
        return new UserEntity(user.id(), user.name(), user.password(), user.email(), user.role(),
                user.address(), user.UUID(), user.createdAt(), user.updatedAt());
    }

    public User toModel() {
        return new User(id, name, password, email, role, UUID, address, createdAt, updatedAt);
    }
}
