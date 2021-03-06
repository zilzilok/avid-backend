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
     * Uses {@link RandomStringUtils#random(int, boolean, boolean)}.
     *
     * @return unique random string
     */
    public static String getUsername() {
        return RandomStringUtils.random(20, true, true);
    }

    /**
     * Uses {@link System#currentTimeMillis()}.
     *
     * @return unique random email (*@test.ru)
     */
    public static String getEmail() {
        return String.format("%d@test.ru", System.currentTimeMillis());
    }

    /**
     * Uses {@link UUID#randomUUID()}.
     *
     * @return random password
     */
    public static String getPassword() {
        return UUID.randomUUID().toString();
    }

    /**
     * Uses {@link RandomStringUtils#random(int, boolean, boolean)}.
     *
     * @return random first name with 10 symbols
     */
    public static String getFirstName() {
        return RandomStringUtils.random(10, true, false);
    }

    /**
     * Uses {@link RandomStringUtils#random(int, boolean, boolean)}.
     *
     * @return random second name with 10 symbols
     */
    public static String getSecondName() {
        return RandomStringUtils.random(10, true, false);
    }

    /**
     * Uses {@link ThreadLocalRandom#nextInt()}.
     *
     * @return random birthdate
     */
    public static String getBirthdate() {
        return new Date(ThreadLocalRandom.current().nextInt() * 1000L).toString();
    }

    /**
     * Uses {@link RandomStringUtils#random(int, boolean, boolean)}.
     *
     * @return random country with 8 symbols
     */
    public static String getCountry() {
        return RandomStringUtils.random(8, true, false);
    }

    /**
     * Uses {@link System#currentTimeMillis()}.
     *
     * @return random photo URL (photo/*)
     */
    public static String getPhotoUrl() {
        return "photo/" + System.currentTimeMillis();
    }

    /**
     * Uses random instance for {@link Random#nextBoolean()}.
     *
     * @return random gender (MALE or FEMALE)
     */
    public static String getGender() {
        return RANDOM_INSTANCE.nextBoolean() ? Gender.FEMALE.getName() : Gender.MALE.getName();
    }
}
