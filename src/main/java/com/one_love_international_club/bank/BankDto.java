package com.one_love_international_club.bank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.one_love_international_club.auth.dto.UserDto;
import com.one_love_international_club.penalty.PenaltyDto;
import com.one_love_international_club.setting.dto.BaseDto;
import com.one_love_international_club.signatory.SignatoryDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankDto extends BaseDto {

    @NotBlank(message = "Bank name is required.")
    private String name;

    @NotBlank(message = "Account number is required.")
    private String accountNumber;

    @NotNull(message = "Balance is required.")
    private BigDecimal balance;

    private UserDto createdBy;

    private Set<SignatoryDto> signatories;

    @JsonIgnore
    private Set<PenaltyDto> incomes;
}
