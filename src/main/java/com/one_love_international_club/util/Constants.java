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

    // Roles
    public static final String ROLE_SYSTEM_ADMIN = "ROLE_SYSTEM_ADMIN";
    public static final String ROLE_TENANT_ADMIN = "ROLE_TENANT_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";

    // Default tenant
    public static final String DEFAULT_TENANT_NAME = "VDT";

    // Error messages
    public static final String ERROR_USER_NOT_FOUND = "User not found with email: ";
    public static final String ERROR_CONTACT_NOT_FOUND = "Contact not found with id: ";
    public static final String ERROR_TENANT_NOT_FOUND = "Tenant not found with id: ";
    public static final String ERROR_INVALID_CREDENTIALS = "Invalid email or password";
    public static final String ERROR_INVALID_2FA_CODE = "Invalid two-factor authentication code";
    public static final String ERROR_CURRENCY_NOT_FOUND = "Currency not found with id: ";

    // Success messages
    public static final String SUCCESS_LOGIN = "Login successful";
    public static final String SUCCESS_LOGOUT = "Logout successful";
    public static final String SUCCESS_PASSWORD_RESET = "Password reset successful";
    public static final String SUCCESS_PASSWORD_RESET_EMAIL = "Password reset email sent successfully";
    public static final String SUCCESS_TENANT_CREATED = "Tenant created successfully";
    public static final String SUCCESS_CONTACT_CREATED = "Contact created successfully";
    public static final String SUCCESS_CURRENCY_CREATED = "Currency created successfully";

    private Constants() {
        // Private constructor to prevent instantiation
    }
}