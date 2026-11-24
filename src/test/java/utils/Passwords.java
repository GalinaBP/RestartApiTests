package utils;

import java.nio.charset.StandardCharsets;

public final class Passwords {
    public static final String VALID = "Qa-valid-pass-1";

    public static final String TOO_SHORT_7 = latin(7);
    public static final String MIN_8 = latin(8);
    public static final String MAX_72 = latin(72);
    public static final String TOO_LONG_73 = latin(73);
    public static final String EMPTY = "";
    public static final String SPACES_ONLY = " ".repeat(8);
    public static final String PADDED = " pass word ";

    public static final String CYRILLIC_36_LETTERS_72_BYTES = cyrillic(36);
    public static final String CYRILLIC_37_LETTERS_74_BYTES = cyrillic(37);
    public static final String CYRILLIC_40_LETTERS_80_BYTES = cyrillic(40);
    public static final String CYRILLIC_72_LETTERS_144_BYTES = cyrillic(72);
    public static final String MIXED_43_CHARS_73_BYTES = cyrillic(30) + latin(13);

    private Passwords() {
    }

    public static String latin(int length) {
        return "a".repeat(length);
    }

    public static String cyrillic(int letters) {
        return "ж".repeat(letters);
    }

    public static int utf8Bytes(String text) {
        return text.getBytes(StandardCharsets.UTF_8).length;
    }
}
