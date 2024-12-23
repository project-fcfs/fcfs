package hanghae.user_service.domain.user;

import java.time.LocalDateTime;

public record User(
        Long id,
        String name,
        String password,
        String email,
        UserRole role,
        String userId,
        String address,
        LocalDateTime createdAt,
        LocalDateTime deletedAt
) {
    public static User normalCreate(String name,String password, String email, String userId, String address,LocalDateTime currentDate) {
        return new User(null, name, password, email, UserRole.ROLE_USER, userId, address, currentDate, null);
    }

    public User decodeData(String decodedName, String decodedEmail,String decodedAddress) {
        return new User(id, decodedName, password, decodedEmail, role, userId, decodedAddress, createdAt, deletedAt);
    }
}
