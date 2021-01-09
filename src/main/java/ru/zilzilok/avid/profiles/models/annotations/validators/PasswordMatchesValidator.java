package ru.zilzilok.avid.profiles.models.annotations.validators;

import ru.zilzilok.avid.profiles.models.annotations.PasswordMatches;
import ru.zilzilok.avid.profiles.models.dto.UserRegDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        if(obj instanceof UserRegDto) {
            UserRegDto userRegDto = (UserRegDto) obj;
            return userRegDto.getPassword().equals(userRegDto.getMatchingPassword());
        }
        return false;
    }
}
