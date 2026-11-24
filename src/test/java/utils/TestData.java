package utils;

import java.util.concurrent.ThreadLocalRandom;

public final class TestData {
    public static final String ID_PREFIX = "qa-";
    public static final String EMAIL_DOMAIN = "restart.test";

    private TestData() {
    }

    public static String uniqueId() {
        return ID_PREFIX + System.currentTimeMillis() + "-"
                + String.format("%06x", ThreadLocalRandom.current().nextInt(0x1000000));
    }

    public static String uniqueEmail() {
        return uniqueId() + "@" + EMAIL_DOMAIN;
    }
}
