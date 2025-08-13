package tr.bel.gaziantep.bysweb.core.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 16.07.2025 15:29
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StrongPasswordValidator.class)
@Documented
public @interface StrongPassword {
    String message() default "Şifre en az 8 karakter uzunluğunda olmalı, en az bir büyük harf, bir küçük harf, bir sayı ve bir özel karakter içermelidir";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int minLength() default 8;
    boolean requireUpper() default true;
    boolean requireLower() default true;
    boolean requireNumber() default true;
    boolean requireSpecial() default true;
}
