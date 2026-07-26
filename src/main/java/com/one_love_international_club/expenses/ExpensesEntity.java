package com.one_love_international_club.expenses;

import com.one_love_international_club.auth.entity.UserEntity;
import com.one_love_international_club.enums.ExpensesType;
import com.one_love_international_club.setting.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.apache.catalina.User;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "expenses")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpensesEntity extends BaseEntity {

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ExpensesType type;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount = BigDecimal.ZERO;

    @Column(name = "paid_by", nullable = false, columnDefinition = "UUID")
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID paidBy;

    @Column(name = "user_id", nullable = false, columnDefinition = "UUID")
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID receiver;

    @Column(name = "proof_of_payment", nullable = false)
    private String proofOfPayment;

    @Column(name = "proof_of_payment_public_id", nullable = false)
    private String proofOfPaymentPublicId;
}
