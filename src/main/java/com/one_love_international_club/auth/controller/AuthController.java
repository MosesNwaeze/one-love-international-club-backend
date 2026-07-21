package com.one_love_international_club.auth.controller;

import com.one_love_international_club.auth.dto.UserRegisterDto;
import com.one_love_international_club.auth.service.AuthService;
import com.one_love_international_club.dto.Response;
import com.one_love_international_club.dto.request.LoginRequestDto;
import com.one_love_international_club.dto.request.PasswordResetRequestDto;
import com.one_love_international_club.dto.request.RegisterRequestDto;
import com.one_love_international_club.dto.response.LoginResponseDto;
import com.one_love_international_club.util.Constants;
import com.one_love_international_club.util.StatusCodeResolver;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Response<UserRegisterDto>> register(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        Response<UserRegisterDto> response = authService.register(registerRequestDto);
        return ResponseEntity
                .status(StatusCodeResolver.getHttpStatus(response.getCode()))
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Response<LoginResponseDto>> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto loginResponse = authService.login(loginRequestDto);
        return ResponseEntity.ok(Response.success(Constants.SUCCESS_LOGIN, loginResponse));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Response<?>> forgotPassword(@RequestParam String email) {
        authService.forgotPassword(email);
        return ResponseEntity.ok(Response.success(Constants.SUCCESS_PASSWORD_RESET_EMAIL));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Response<?>> resetPassword(@Valid @RequestBody PasswordResetRequestDto passwordResetRequestDto) {
        authService.resetPassword(passwordResetRequestDto);
        return ResponseEntity.ok(Response.success(Constants.SUCCESS_PASSWORD_RESET));
    }

    @PostMapping("/logout")
    public ResponseEntity<Response<?>> logout(@RequestParam String refreshToken) {
        authService.logout(refreshToken);
        return ResponseEntity.ok(Response.success(Constants.SUCCESS_LOGOUT));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Response<LoginResponseDto>> refreshToken(@RequestParam String refreshToken) {
        LoginResponseDto loginResponse = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(Response.success("Token refreshed successfully", loginResponse));
    }
}
