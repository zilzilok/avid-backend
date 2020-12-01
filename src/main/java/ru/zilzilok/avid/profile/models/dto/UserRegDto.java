package ru.zilzilok.avid.profile.models.dto;

import lombok.*;
import ru.zilzilok.avid.profile.annotations.PasswordMatches;
import ru.zilzilok.avid.profile.annotations.ValidEmail;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@PasswordMatches
public class UserRegDto {
    @NotNull
    @NotEmpty
    private String username;

    @NotNull
    @NotEmpty
    private String password;
    private String matchingPassword;

    @NotNull
    @NotEmpty
    @ValidEmail
    private String email;
}
