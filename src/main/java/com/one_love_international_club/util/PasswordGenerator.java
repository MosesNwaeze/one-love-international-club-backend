package com.api.vdtcommsws.util;

import java.security.SecureRandom;
import java.util.Random;

public class PasswordGenerator {

    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String ALL_CHARS = LOWER + UPPER + DIGITS;

    private static final Random RANDOM = new SecureRandom();

    /**
     * Generate a secure random password that meets complexity requirements.
     * - At least one lowercase letter
     * - At least one uppercase letter
     * - At least one digit
     * - Minimum length of 10 characters
     *
     * @return A secure random password
     */
    public static String generateSecurePassword() {
        return generateSecurePassword(10);
    }

    /**
     * Generate a secure random password with specified length.
     *
     * @param length The length of the password to generate
     * @return A secure random password
     */
    public static String generateSecurePassword(int length) {
        if (length < 8) {
            throw new IllegalArgumentException("Password length must be at least 8 characters");
        }

        // Ensure at least one character from each required category
        char[] password = new char[length];

        password[0] = LOWER.charAt(RANDOM.nextInt(LOWER.length()));
        password[1] = UPPER.charAt(RANDOM.nextInt(UPPER.length()));
        password[2] = DIGITS.charAt(RANDOM.nextInt(DIGITS.length()));

        // Fill the rest of the password with random characters (letters or digits)
        for (int i = 3; i < length; i++) {
            password[i] = ALL_CHARS.charAt(RANDOM.nextInt(ALL_CHARS.length()));
        }

        // Shuffle the password
        for (int i = 0; i < length; i++) {
            int randomIndex = RANDOM.nextInt(length);
            char temp = password[i];
            password[i] = password[randomIndex];
            password[randomIndex] = temp;
        }

        return new String(password);
    }

    private PasswordGenerator() {
        // Private constructor to prevent instantiation
    }
}