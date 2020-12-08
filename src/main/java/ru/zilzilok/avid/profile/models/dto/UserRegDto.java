package ru.zilzilok.avid.profile.models.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.zilzilok.avid.profile.models.annotations.PasswordMatches;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@PasswordMatches
public class UserRegDto {
    @NotNull(message = "username can't be null")
    @NotEmpty(message = "username can't be empty")
    private String username;

    @NotNull(message = "password can't be null")
    @NotEmpty(message = "password can't be empty")
    private String password;
    
    @NotNull(message = "matchingPassword can't be null")
    @NotEmpty(message = "matchingPassword can't be empty")
    private String matchingPassword;

    @NotNull(message = "email can't be null")
    @NotEmpty(message = "email can't be empty")
    @Email
    private String email;
}
