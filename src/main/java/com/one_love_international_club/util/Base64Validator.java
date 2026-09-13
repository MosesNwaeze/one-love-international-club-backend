package com.one_love_international_club.util;

import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

@UtilityClass
public class Base64Validator {

    private static final Pattern DATA_URL_PATTERN = Pattern
            .compile("^data:([a-zA-Z]+/([a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,})?(;base64)?,([a-zA-Z0-9+/=]+)$");

    public static boolean isValidBase64(String base64) {
        return DATA_URL_PATTERN.matcher(base64).matches();
    }
}
