package hanghae.user_service.controller.resp;

import hanghae.user_service.domain.user.User;
import java.time.LocalDateTime;

public record UserCreateRespDto(
        String name,
        String userId,
        LocalDateTime createdAt
) {
    public static UserCreateRespDto of(User user) {
        return new UserCreateRespDto(user.name(), user.userId(), user.createdAt());
    }
}
