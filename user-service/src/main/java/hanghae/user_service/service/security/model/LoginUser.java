package hanghae.user_service.service.security.model;

import hanghae.user_service.domain.user.User;
import hanghae.user_service.domain.user.UserRole;

public record LoginUser(
        Long id,
        String name,
        String password,
        String email,
        UserRole userRole,
        String UUID
) {

    public static LoginUser create(User user) {
        return new LoginUser(
                user.id(), user.name(), user.password(),
                user.email(), user.role(), user.UUID()
        );
    }

}
