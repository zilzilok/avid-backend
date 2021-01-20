package ru.zilzilok.avid.profiles.models.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Pattern(regexp = "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,}[a-zA-Z0-9]$", message = "Please provide a valid username with length = [5, 20].")
@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface ValidUsername {
    String message() default "Please provide a valid username with length = [5, 20].";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
