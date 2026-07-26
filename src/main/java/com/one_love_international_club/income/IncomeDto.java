package com.one_love_international_club.income;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.one_love_international_club.bank.BankDto;
import com.one_love_international_club.enums.IncomeType;
import com.one_love_international_club.setting.dto.BaseDto;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IncomeDto extends BaseDto {
    @NotNull(message = "Income type is required.")
    private IncomeType incomeType;

    @NotNull(message = "Amount is required.")
    private BigDecimal amount;

    @JsonIgnore
    private UUID paidBy;

    private BankDto bank;

    private String proofOfPayment;

    private String proofOfPaymentPublicId;

    private PaidBy madeBy;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaidBy {
        private String firstName;
        private String lastName;
        private UUID id;
    }
}
