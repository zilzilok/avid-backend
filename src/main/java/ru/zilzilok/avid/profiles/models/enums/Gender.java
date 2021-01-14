package ru.zilzilok.avid.profiles.models.enums;

import org.apache.commons.lang3.StringUtils;

public enum Gender {
    MALE("male"), FEMALE("female");

    private final String name;

    Gender(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Gender fromString(String str) {
        for (Gender gender : Gender.values()) {
            if(StringUtils.equalsIgnoreCase(gender.getName(), StringUtils.trim(str))) {
                return gender;
            }
        }
        return null;
    }
}
