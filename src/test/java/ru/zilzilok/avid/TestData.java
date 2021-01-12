package ru.zilzilok.avid;

import java.util.UUID;

public class TestData {
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
}
