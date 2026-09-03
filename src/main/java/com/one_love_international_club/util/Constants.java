package com.one_love_international_club.util;

public class Constants {

    // General
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_SORT_BY = "createdAt";
    public static final String DEFAULT_SORT_DIRECTION = "desc";
    public static final String API_VERSION = "v1";

    // JWT
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";


    // Error messages
    public static final String ERROR_USER_NOT_FOUND = "User not found with email: ";
    public static final String ERROR_INVALID_CREDENTIALS = "Invalid email or password";

    // Success messages
    public static final String SUCCESS_LOGIN = "Login successful";
    public static final String SUCCESS_LOGOUT = "Logout successful";
    public static final String SUCCESS_PASSWORD_RESET = "Password reset successful";
    public static final String SUCCESS_PASSWORD_RESET_EMAIL = "Password reset email sent successfully";

    private Constants() {
        // Private constructor to prevent instantiation
    }
}