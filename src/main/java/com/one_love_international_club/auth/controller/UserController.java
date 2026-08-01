package com.one_love_international_club.auth.controller;


import com.one_love_international_club.auth.dto.ApproveUserDto;
import com.one_love_international_club.auth.dto.UserDto;
import com.one_love_international_club.auth.service.AuthService;
import com.one_love_international_club.setting.dto.Response;
import com.one_love_international_club.setting.dto.response.PaginatedResponse;
import com.one_love_international_club.util.StatusCodeResolver;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Controller")
public class UserController {

    private final AuthService authService;

    @PutMapping("/approve-user")
    @PreAuthorize("hasRole('Chief Provost')")
    public ResponseEntity<Response<Void>> approveUser(
            @Valid @RequestBody ApproveUserDto approveUserDto
    ) {
        Response<Void> response = authService.approveUser(approveUserDto);
        return ResponseEntity
                .status(StatusCodeResolver.getHttpStatus(response.getCode()))
                .body(response);
    }

    @GetMapping("/all-unapproved-users")
    @PreAuthorize("hasRole('Chief Provost')")
    public ResponseEntity<Response<PaginatedResponse<UserDto>>> findAllUnapprovedUsers(
            @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(value = "size", defaultValue = "5", required = false) Integer size
    ) {
        Response<PaginatedResponse<UserDto>> response = authService
                .findAllUnapprovedUsers(page, size);
        return ResponseEntity
                .status(StatusCodeResolver.getHttpStatus(response.getCode()))
                .body(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('Chief Provost')")
    public ResponseEntity<Response<PaginatedResponse<UserDto>>> findAllUsers(
            @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(value = "size", defaultValue = "5", required = false) Integer size
    ) {
        Response<PaginatedResponse<UserDto>> response = authService
                .getAllUsers(page, size);
        return ResponseEntity
                .status(StatusCodeResolver.getHttpStatus(response.getCode()))
                .body(response);
    }

    @PutMapping("/update-user")
    public ResponseEntity<Response<UserDto>> updateUser(
            @RequestBody UserDto userDto
    ) {
        Response<UserDto> response = authService.updateUser(userDto);
        return ResponseEntity
                .status(StatusCodeResolver.getHttpStatus(response.getCode()))
                .body(response);
    }


}
