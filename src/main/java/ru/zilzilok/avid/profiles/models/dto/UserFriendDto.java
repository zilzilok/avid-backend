package ru.zilzilok.avid.profiles.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.zilzilok.avid.profiles.models.entities.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFriendDto {
    User friend;
    boolean has;
}
