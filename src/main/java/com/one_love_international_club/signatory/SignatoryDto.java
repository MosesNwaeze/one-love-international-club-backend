package com.one_love_international_club.signatory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.one_love_international_club.auth.dto.UserDto;
import com.one_love_international_club.auth.entity.UserEntity;
import com.one_love_international_club.bank.BankDto;
import com.one_love_international_club.bank.BankEntity;
import com.one_love_international_club.setting.dto.BaseDto;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignatoryDto extends BaseDto {
    @NotNull(message = "User is required.")
    private UserDto signatory;
    @JsonIgnore
    private BankDto bank;
    private UserDto createdBy;
}
