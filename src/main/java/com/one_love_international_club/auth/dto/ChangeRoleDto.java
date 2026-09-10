package com.one_love_international_club.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeRoleDto {
    @NotNull(message = "User id is required.")
    private UUID userId;

    @NotNull(message = "Role id is required.")
    private UUID roleId;
}
