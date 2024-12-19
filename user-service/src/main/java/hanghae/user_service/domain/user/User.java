package hanghae.user_service.domain.user;

import java.time.LocalDateTime;

public record User(
        Long id,
        String name,
        String password,
        String email,
        UserRole role,
        String UUID,
        String address,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static User normalCreate(String name,String password, String email, String UUID, String address,LocalDateTime currentDate) {
        return new User(null, name, password, email, UserRole.ROLE_USER, UUID, address, currentDate, currentDate);
    }
}
