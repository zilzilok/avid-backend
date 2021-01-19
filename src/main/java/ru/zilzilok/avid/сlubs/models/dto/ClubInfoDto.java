package ru.zilzilok.avid.сlubs.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClubInfoDto {
    private String name;
    private String description;
    private String descriptionShort;
    private String photoUrl;
}
