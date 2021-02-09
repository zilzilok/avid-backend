package ru.zilzilok.avid.clubs;

import org.apache.commons.lang3.RandomStringUtils;

public class ClubTestData {
    /**
     * Uses {@link System#currentTimeMillis()}.
     *
     * @return random string
     */
    public static String getName() {
        return String.valueOf(System.currentTimeMillis());
    }

    /**
     * Uses {@link RandomStringUtils#random(int, boolean, boolean)}.
     *
     * @return random description with 20 symbols
     */
    public static String getDescription() {
        return RandomStringUtils.random(20, true, false);
    }

    /**
     * Uses {@link RandomStringUtils#random(int, boolean, boolean)}.
     *
     * @return random short description with 10 symbols
     */
    public static String getDescriptionShort() {
        return RandomStringUtils.random(10, true, false);
    }

    /**
     * Uses {@link System#currentTimeMillis()}.
     *
     * @return random photo URL (photo/*)
     */
    public static String getPhotoUrl() {
        return "photo/" + System.currentTimeMillis();
    }
}
