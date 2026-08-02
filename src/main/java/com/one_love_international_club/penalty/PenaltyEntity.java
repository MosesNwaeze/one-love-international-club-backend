package com.one_love_international_club.penalty;

import com.one_love_international_club.bank.BankEntity;
import com.one_love_international_club.due.DueEntity;
import com.one_love_international_club.setting.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "incomes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PenaltyEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dues_id", nullable = false)
    private DueEntity due;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "proof_of_payment", nullable = false)
    private String proofOfPayment;

    @Column(name = "proof_of_payment_public_id", nullable = false)
    private String proofOfPaymentPublicId;

    @Column(name = "paid_by", nullable = false, columnDefinition = "UUID")
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID paidBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id", nullable = false, columnDefinition = "UUID")
    @JdbcTypeCode(SqlTypes.UUID)
    private BankEntity bank;
}
