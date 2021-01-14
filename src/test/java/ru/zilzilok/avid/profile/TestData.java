package ru.zilzilok.avid.profile;

import org.apache.commons.lang3.RandomStringUtils;
import ru.zilzilok.avid.profiles.models.enums.Gender;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class TestData {

    private static final Random RANDOM_INSTANCE = new Random();

    /**
     * Uses randomUUID from {@link UUID} and currentTimeMillis from {@link System}.
     *
     * @return unique random string
     */
    public static String getUsername() {
        return String.format("%s%s", UUID.randomUUID(), System.currentTimeMillis());
    }

    /**
     * Uses currentTimeMillis from {@link System}.
     *
     * @return unique random email
     */
    public static String getEmail() {
        return String.format("%d@test.ru", System.currentTimeMillis());
    }

    /**
     * Uses randomUUID from {@link UUID}.
     *
     * @return random password
     */
    public static String getPassword() {
        return UUID.randomUUID().toString();
    }

    /**
     *
     * @return
     */
    public static String getFirstName() {
        return RandomStringUtils.random(10, true, false);
    }

    public static String getSecondName() {
        return RandomStringUtils.random(10, true, false);
    }

    public static Date getBirthdate() {
        return null;
    }

    public static String getCountry() {
        return RandomStringUtils.random(8, true, false);
    }

    public static String getPhotoPath() {
        return "photo/" + System.currentTimeMillis();
    }

    public static String getGender() {
        return RANDOM_INSTANCE.nextBoolean() ? Gender.FEMALE.toString() : Gender.MALE.toString();
    }
}
