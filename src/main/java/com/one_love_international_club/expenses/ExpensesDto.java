package com.one_love_international_club.expenses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.one_love_international_club.enums.ExpensesType;
import com.one_love_international_club.setting.dto.BaseDto;
import jakarta.validation.constraints.NotBlank;
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
public class ExpensesDto extends BaseDto {
    @NotNull(message = "Expense type is required.")
    private ExpensesType type;

    @NotNull(message = "Expense amount is required.")
    private BigDecimal amount;

    @JsonIgnore
    private UUID paidBy;

    private UUID receiver;

    @NotBlank(message = "Proof of payment is required.")
    private String proofOfPayment;

    private String proofOfPaymentPublicId;

    private Details paymentMadeBy;

    private Details paymentReceivedBy;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Details {
        private String firstName;
        private String lastName;
        private UUID id;
    }
}
