package ru.zilzilok.avid.profile.models.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.zilzilok.avid.profile.models.enums.Gender;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.sql.Date;

@Data
@NoArgsConstructor
public class UserInfoDto {
    private String firstName;
    private String secondName;
    private Date birthdate;
    private String country;
    private String photoPath;

    @Enumerated(EnumType.STRING)
    @EqualsAndHashCode.Exclude
    private Gender gender;
}
