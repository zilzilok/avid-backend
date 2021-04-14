package ru.zilzilok.avid.profiles.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.zilzilok.avid.profiles.models.other.UserGame;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserGameHasDto {
    UserGame userGame;
    boolean has;
}
