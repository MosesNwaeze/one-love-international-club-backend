package com.one_love_international_club.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response<T> {
    private Status status;
    private String message;
    private Integer code;
    private T data;
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    // Success responses
    public static <T> Response<T> success(String message, T data) {
        return Response.<T>builder()
                .status(Status.SUCCESSFUL)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> Response<T> success(T data) {
        return success("Operation completed successfully", data);
    }

    public static Response<?> success(String message) {
        return success(message, null);
    }

    // Error responses
    public static Response<?> error(String message) {
        return error(message, null, null);
    }

    public static Response<?> error(String message, Integer code) {
        return error(message, code, null);
    }

    public static <T> Response<T> error(String message, Integer code, T errorData) {
        return Response.<T>builder()
                .status(Status.ERROR)
                .message(message)
                .code(code)
                .data(errorData)
                .build();
    }

    // Fluent API methods - manually implemented to avoid private access issues
    public Response<T> data(Map<String, String> data) {
        this.data = (T) data;
        return this;
    }

    public Response<T> code(int code) {
        this.code = code;
        return this;
    }

    public Response<T> message(String message) {
        this.message = message;
        return this;
    }
}