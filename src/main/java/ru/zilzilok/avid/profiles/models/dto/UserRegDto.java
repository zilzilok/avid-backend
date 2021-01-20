package ru.zilzilok.avid.profiles.models.dto;

import lombok.*;
import ru.zilzilok.avid.profiles.models.annotations.PasswordMatches;
import ru.zilzilok.avid.profiles.models.annotations.ValidEmail;
import ru.zilzilok.avid.profiles.models.annotations.ValidUsername;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@PasswordMatches
public class UserRegDto {
    @NotNull(message = "username can't be null")
    @NotEmpty(message = "username can't be empty")
    @ValidUsername
    private String username;

    @NotNull(message = "password can't be null")
    @NotEmpty(message = "password can't be empty")
    private String password;
    
    @NotNull(message = "matchingPassword can't be null")
    @NotEmpty(message = "matchingPassword can't be empty")
    private String matchingPassword;

    @NotNull(message = "email can't be null")
    @NotEmpty(message = "email can't be empty")
    @ValidEmail
    private String email;
}
