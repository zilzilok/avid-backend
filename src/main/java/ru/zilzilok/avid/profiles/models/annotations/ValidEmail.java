package ru.zilzilok.avid.profiles.models.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Email(message = "Please provide a valid email address")
@Pattern(regexp = "(?im)^(?=.{1,64}@)(?:(\"[^\"\\\\]*(?:\\\\.[^\"\\\\]*)*\"@)|((?:[0-9a-z](?:\\.(?!\\.)|[-!#\\$%&'\\*\\+/=\\?\\^`\\{\\}\\|~\\w])*)?[0-9a-z]@))(?=.{1,255}$)(?:(\\[(?:\\d{1,3}\\.){3}\\d{1,3}\\])|((?:(?=.{1,63}\\.)[0-9a-z][-\\w]*[0-9a-z]*\\.)+[a-z0-9][\\-a-z0-9]{0,22}[a-z0-9])|((?=.{1,63}$)[0-9a-z][-\\w]*))$", message = "Please provide a valid email address")
@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface ValidEmail {
    String message() default "Please provide a valid email address";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}