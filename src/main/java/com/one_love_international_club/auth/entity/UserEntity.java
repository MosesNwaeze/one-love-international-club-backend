package com.one_love_international_club.auth.entity;

import com.one_love_international_club.committee.CommitteeEntity;
import com.one_love_international_club.setting.dto.Status;
import com.one_love_international_club.setting.entity.BaseEntity;
import com.one_love_international_club.setting.entity.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.one_love_international_club.enums.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends BaseEntity {

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "other_name")
    private String otherName;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false, columnDefinition = "UUID")
    @JdbcTypeCode(SqlTypes.UUID)
    private Role role;

    @Column(name = "title")
    @Enumerated(EnumType.STRING)
    private Title title;

    @Column(name = "current_address", nullable = false, columnDefinition = "TEXT")
    private String currentAddress;

    @Column(name = "permanent_address", columnDefinition = "TEXT")
    private String permanentAddress;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "marital_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;

    @Column(name = "occupation", nullable = false)
    private String occupation;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "profile_pic")
    private String profilePic;

    @Column(name = "pic_public_id")
    private String picPublicId;

    @Column(name = "is_root_admin")
    private Boolean isRootAdmin = Boolean.FALSE;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "guarantor", columnDefinition = "UUID")
    @JdbcTypeCode(SqlTypes.UUID)
    private UserEntity guarantor;

    @Column(name = "letter_of_undertaking")
    private String letterOfUndertaking;

    @Column(name = "letter_of_undertaking_public_id")
    private String letterOfUndertakingPublicId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "committee", columnDefinition = "UUID")
    @JdbcTypeCode(SqlTypes.UUID)
    private CommitteeEntity committee;

    @Column(name = "bank_account_name")
    private String bankAccountName;

    @Column(name = "bank_account_number")
    private String bankAccountNumber;

    @Column(name = "registration_fee_url")
    private String registrationFeeUrl;

    @Column(name = "registration_fee_public_id")
    private String registrationFeePublicId;

    @Column(name = "registration_form_url")
    private String registrationFormUrl;

    @Column(name = "registration_form_public_id")
    private String registrationFormPublic;

}
