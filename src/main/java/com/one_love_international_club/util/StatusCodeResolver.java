package com.one_love_international_club.util;

import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

public class StatusCodeResolver {

    private static final List<Integer> CODES = Arrays.stream(HttpStatus.values())
            .map(HttpStatus::value).toList();

    public static HttpStatus getHttpStatus(int code) {

        if (CODES.contains(code)) {
            return HttpStatus.resolve(code);
        }

        return HttpStatus.REQUEST_TIMEOUT;
    }
}
