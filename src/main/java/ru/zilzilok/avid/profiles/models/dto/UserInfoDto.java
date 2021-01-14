package ru.zilzilok.avid.profiles.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.zilzilok.avid.profiles.models.enums.Gender;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {
    private String firstName;
    private String secondName;
    private Date birthdate;
    private String country;
    private String photoPath;

    @Enumerated(EnumType.STRING)
    @EqualsAndHashCode.Exclude
    private Gender gender;

    public void setBirthdate(String birthdateStr) {
        try {
            this.birthdate = Date.valueOf(birthdateStr);
        } catch (IllegalArgumentException ignored) {
        }
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public static UserInfoDtoBuilder builder() {
        return new UserInfoDto().new UserInfoDtoBuilder();
    }

    public class UserInfoDtoBuilder {

        public UserInfoDtoBuilder firstName(String firstName) {
            UserInfoDto.this.setFirstName(firstName);
            return this;
        }

        public UserInfoDtoBuilder secondName(String secondName) {
            UserInfoDto.this.setSecondName(secondName);
            return this;
        }

        public UserInfoDtoBuilder birthdate(String birthdate) {
            UserInfoDto.this.setBirthdate(birthdate);
            return this;
        }

        public UserInfoDtoBuilder birthdate(Date birthdate) {
            UserInfoDto.this.setBirthdate(birthdate);
            return this;
        }

        public UserInfoDtoBuilder country(String country) {
            UserInfoDto.this.setCountry(country);
            return this;
        }

        public UserInfoDtoBuilder photoPath(String photoPath) {
            UserInfoDto.this.setPhotoPath(photoPath);
            return this;
        }

        public UserInfoDtoBuilder gender(Gender gender) {
            UserInfoDto.this.setGender(gender);
            return this;
        }

        public UserInfoDto build() {
            return UserInfoDto.this;
        }
    }
}
