package tr.bel.gaziantep.bysweb.core.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 16.07.2025 15:30
 */
public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {

    private int minLength;
    private boolean requireUpper;
    private boolean requireLower;
    private boolean requireNumber;
    private boolean requireSpecial;

    @Override
    public void initialize(StrongPassword constraintAnnotation) {
        this.minLength = constraintAnnotation.minLength();
        this.requireUpper = constraintAnnotation.requireUpper();
        this.requireLower = constraintAnnotation.requireLower();
        this.requireNumber = constraintAnnotation.requireNumber();
        this.requireSpecial = constraintAnnotation.requireSpecial();
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.isEmpty()) {
            addConstraintViolation(context, "Parola boş olamaz...");
            return false;
        }

        if (password.length() < minLength) {
            addConstraintViolation(context, "Parola en az" + minLength + " olmalıdır");
            return false;
        }

        if (requireUpper && !Pattern.compile("[A-Z]").matcher(password).find()) {
            addConstraintViolation(context, "Parola en az bir büyük harf içermelidir");
            return false;
        }

        if (requireLower && !Pattern.compile("[a-z]").matcher(password).find()) {
            addConstraintViolation(context, "Şifre en az bir küçük harf içermelidir");
            return false;
        }

        if (requireNumber && !Pattern.compile("[0-9]").matcher(password).find()) {
            addConstraintViolation(context, "Şifre en az bir rakama içermelidir");
            return false;
        }

        if (requireSpecial && !Pattern.compile("[^a-zA-Z0-9]").matcher(password).find()) {
            addConstraintViolation(context, "Şifre en az bir özel karakter içermelidir");
            return false;
        }

        return true;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}