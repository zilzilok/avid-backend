package ru.zilzilok.avid.boardgames.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.zilzilok.avid.profiles.models.entities.User;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameOwnerDto {
    User owner;
    String review;
    double rating;
    Timestamp creatingDateTime;
}
