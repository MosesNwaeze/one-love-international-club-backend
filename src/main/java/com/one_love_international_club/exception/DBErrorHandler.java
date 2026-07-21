package com.one_love_international_club.exception;

import org.springframework.dao.DataIntegrityViolationException;

import java.sql.SQLException;

public class DBErrorHandler {

    public static ClubException handleError(DataIntegrityViolationException ex) {

        Throwable rootCause = ex.getRootCause();

        if (rootCause instanceof SQLException sqlException) {
            String message = sqlException.getMessage().toLowerCase();

            if (isDuplicateKeyViolation(message)) {
                return new ClubException(ErrorCode.DUPLICATE_KEY,
                        "The record you are trying to create or update already exists.", ex);
            } else if (isNotNullConstraintViolation(message) || isForeignKeyViolation(message)) {
                return new ClubException(ErrorCode.INVALID_REQUEST,
                        "Invalid request: A required field is missing or null.", ex);
            } else {
                return new ClubException(ErrorCode.INTEGRITY_VIOLATION, "Data integrity violation", ex);
            }
        } else {
            return new ClubException(ErrorCode.INTEGRITY_VIOLATION, "Unexpected data integrity violation", ex);
        }
    }


    private static boolean isDuplicateKeyViolation(String message) {

        return message.contains("duplicate");
    }

    private static boolean isNotNullConstraintViolation(String message) {

        return message.contains("not-null") || message.contains("null value");
    }

    private static boolean isForeignKeyViolation(String message) {

        return message.contains("foreign key");
    }

}
