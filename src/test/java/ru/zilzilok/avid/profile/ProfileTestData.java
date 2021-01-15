package ru.zilzilok.avid.profile;

import org.apache.commons.lang3.RandomStringUtils;
import ru.zilzilok.avid.profiles.models.enums.Gender;

import java.sql.Date;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Helps to randomize profile tests.
 */
public class ProfileTestData {

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
     * Uses random from {@link RandomStringUtils}.
     *
     * @return random first name with 10 symbols
     */
    public static String getFirstName() {
        return RandomStringUtils.random(10, true, false);
    }

    /**
     * Uses random from {@link RandomStringUtils}.
     *
     * @return random second name with 10 symbols
     */
    public static String getSecondName() {
        return RandomStringUtils.random(10, true, false);
    }

    /**
     * Uses current().nextInt() from {@link ThreadLocalRandom}.
     *
     * @return random birthdate
     */
    public static String getBirthdate() {
        return new Date(ThreadLocalRandom.current().nextInt() * 1000L).toString();
    }

    /**
     * Uses random from {@link RandomStringUtils}.
     *
     * @return random country with 8 symbols
     */
    public static String getCountry() {
        return RandomStringUtils.random(8, true, false);
    }

    /**
     * Uses random from {@link RandomStringUtils}.
     *
     * @return random second name with 10 symbols
     */
    public static String getPhotoPath() {
        return "photo/" + System.currentTimeMillis();
    }

    /**
     * Uses random instance from {@link Random} for nextBoolean.
     *
     * @return random gender (MALE or FEMALE)
     */
    public static String getGender() {
        return RANDOM_INSTANCE.nextBoolean() ? Gender.FEMALE.getName() : Gender.MALE.getName();
    }
}