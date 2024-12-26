package hanghae.user_service.controller.resp;

import hanghae.user_service.domain.user.User;
import java.time.LocalDateTime;

public record UserInfoRespDto(
        String email,
        String name,
        String address,
        String userId,
        LocalDateTime createdAt

) {
    public static UserInfoRespDto of(User user) {
        return new UserInfoRespDto(user.email(), user.name(), user.address(), user.userId(), user.createdAt());
    }
}
