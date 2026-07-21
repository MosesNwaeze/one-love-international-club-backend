package com.one_love_international_club.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetRequestDto {

    @Email(message = "Email must be valid")
    @NotBlank(message = "Email is required")
    private String email;

    private String token;

    private String newPassword;
}