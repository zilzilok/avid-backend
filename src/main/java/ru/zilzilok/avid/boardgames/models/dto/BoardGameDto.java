package ru.zilzilok.avid.boardgames.models.dto;

import lombok.Data;

@Data
public class BoardGameDto {

    private String description;
    private String descriptionShort;
    private String photoUrl;
    private String title;
    private int year;
}
