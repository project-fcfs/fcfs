package hanghae.user_service.controller.valid;

import hanghae.user_service.service.UserService;
import hanghae.user_service.service.common.exception.CustomApiException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserEmailValidator implements ConstraintValidator<UserEmailUnique, String> {
    private final UserService userService;

    public UserEmailValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void initialize(UserEmailUnique constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(isEmpty(s)) return false;
        return !hasDuplicateEmail(s);
    }

    private boolean isEmpty(String value) {
        return value == null || value.isBlank();
    }

    private boolean hasDuplicateEmail(String value) {
        try {
            userService.checkDuplicateEmail(value);
            return false;
        } catch (CustomApiException e) {
            return true;
        }
    }
}
