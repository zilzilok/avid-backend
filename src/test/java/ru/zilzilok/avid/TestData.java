package ru.zilzilok.avid;

import java.util.UUID;

public class TestData {

    public static String getRandomUsername() {
        return String.format("%s%s", UUID.randomUUID(), System.currentTimeMillis());
    }
}
