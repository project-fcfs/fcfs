package hanghae.user_service.domain.user;

import java.time.LocalDateTime;

public record User(
        Long id,
        String name,
        String password,
        String email,
        UserRole role,
        String UUID,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static User normalCreate(String name,String password, String email, String UUID, LocalDateTime currentDate) {
        return new User(null, name, password, email, UserRole.ROLE_USER, UUID, currentDate, currentDate);
    }
}
