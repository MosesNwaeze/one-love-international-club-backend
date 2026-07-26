package com.one_love_international_club.signatory;

import com.one_love_international_club.auth.entity.UserEntity;
import com.one_love_international_club.bank.BankEntity;
import com.one_love_international_club.setting.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "signatories")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignatoryEntity extends BaseEntity {
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "signatory", columnDefinition = "UUID", unique = true, nullable = false)
    @JdbcTypeCode(SqlTypes.UUID)
    private UserEntity signatory;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", columnDefinition = "UUID", nullable = false)
    @JdbcTypeCode(SqlTypes.UUID)
    private UserEntity createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id", columnDefinition = "UUID")
    @JdbcTypeCode(SqlTypes.UUID)
    private BankEntity bank;
}
