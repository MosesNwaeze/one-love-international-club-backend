package com.one_love_international_club.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TwoFactorRequestDto {

    @NotNull
    private UUID twoFactorToken;

    @NotBlank(message = "Code is required")
    private String code;

    @NotBlank(message = "Email is required")
    private String email;
}