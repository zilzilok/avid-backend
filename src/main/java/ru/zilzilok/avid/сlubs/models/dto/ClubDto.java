package ru.zilzilok.avid.сlubs.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClubDto {
    @NotNull(message = "club name can't be null")
    @NotEmpty(message = "club name can't be empty")
    private String name;
    private String description;
    private String descriptionShort;
    private String photoUrl;
}
