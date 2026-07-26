package com.one_love_international_club.bank;

import com.one_love_international_club.auth.entity.UserEntity;
import com.one_love_international_club.income.IncomeEntity;
import com.one_love_international_club.setting.entity.BaseEntity;
import com.one_love_international_club.signatory.SignatoryEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "banks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankEntity extends BaseEntity {

    @Column(name = "name",  nullable = false, unique = true)
    private String name;

    @Column(name = "account_number",nullable = false)
    private String accountNumber;

    @Column(name = "total_amount",nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private UserEntity createdBy;

    @OneToMany(mappedBy = "bank")
    private Set<SignatoryEntity> signatories = new HashSet<>();

    @OneToMany(mappedBy = "bank")
    private Set<IncomeEntity> incomes = new HashSet<>();
}
