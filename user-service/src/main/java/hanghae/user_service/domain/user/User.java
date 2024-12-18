package hanghae.user_service.domain.user;

import java.time.LocalDateTime;

public record User(
        Long id,
        String password,
        String email,
        UserRole role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static User normalCreate(String password, String email, LocalDateTime currentDate) {
        return new User(null, password, email, UserRole.ROLE_USER, currentDate, currentDate);
    }
}
