package com.one_love_international_club.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.one_love_international_club.enums.ApprovalStatus;
import com.one_love_international_club.setting.dto.RoleDto;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApproveUserDto {
    @NotNull(message = "Role id is required.")
    private UUID roleId;
    @NotNull(message = "User id is required.")
    private UUID userId;
    @NotNull(message = "Approval status is required.")
    private ApprovalStatus approvalStatus;
    private String approvalComment;

}
