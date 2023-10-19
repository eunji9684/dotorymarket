package com.fullstack2.dotori;

import java.security.SecureRandom;
import java.util.Random;

public class PasswordGenerator {

    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";
    private static final String SPECIAL_CHAR = "!@#$%&*()_+-=[]|,./?><";

    private static final String PASSWORD_ALLOW = CHAR_LOWER + CHAR_UPPER + NUMBER + SPECIAL_CHAR;
    private static final Random random = new SecureRandom();

    public static String generateRandomPassword(int length) {
        if (length < 1) throw new IllegalArgumentException();

        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomCharIndex = random.nextInt(PASSWORD_ALLOW.length());
            password.append(PASSWORD_ALLOW.charAt(randomCharIndex));
        }
        return password.toString();
    }
}
