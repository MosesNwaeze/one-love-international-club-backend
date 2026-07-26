package com.one_love_international_club.benefits;

import com.one_love_international_club.auth.entity.UserEntity;
import com.one_love_international_club.setting.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "benefits")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BenefitEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JdbcTypeCode(SqlTypes.UUID)
    @JoinColumn(name = "user_id", columnDefinition = "UUID", nullable = false)
    private UserEntity user;

    @Column(name = "amount_received", nullable = false)
    private BigDecimal amountReceived = BigDecimal.ZERO;
}
