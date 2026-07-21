package com.api.vdtcommsws.auth.dto;

import com.api.vdtcommsws.dto.BaseDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDto extends BaseDto {

    private UUID id;
    private String email;
    @JsonIgnore
    private String password;
    private boolean isActive;
    private String secretKey;
    private boolean isTwoFactorRegistered;
    private String qrCode;
    private LocalDateTime emailVerifiedAt;
    private LocalDateTime mobileVerifiedAt;


}
