package hanghae.user_service.domain.user;

public enum UserRole {
    ROLE_USER("일반"), ROLE_ADMIN("관리자");

    String description;

    UserRole(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
