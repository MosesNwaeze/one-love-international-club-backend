package com.one_love_international_club.resignation;

import com.one_love_international_club.auth.entity.UserEntity;
import com.one_love_international_club.benefits.BenefitEntity;
import com.one_love_international_club.enums.ResignationStatus;
import com.one_love_international_club.enums.ResignationType;
import com.one_love_international_club.setting.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "resignations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResignationEntity extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JdbcTypeCode(SqlTypes.UUID)
    @JoinColumn(name = "user_id", columnDefinition = "UUID", nullable = false, unique = true)
    private UserEntity user;

    @Column(name = "resignation_letter")
    private String resignationLetter;

    @Column(name = "resignation_letter_public_id")
    private String resignationLetterPublicId;

    @Column(name = "reason_of_resignation", columnDefinition = "TEXT")
    private String reasonOfResignation;

    @Column(name = "resignation_status")
    @Enumerated(EnumType.STRING)
    private ResignationStatus resignationStatus = ResignationStatus.PENDING;

    @Column(name = "resignation_type")
    @Enumerated(EnumType.STRING)
    private ResignationType resignationType = ResignationType.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "benefit_id", columnDefinition = "UUID")
    @JdbcTypeCode(SqlTypes.UUID)
    private BenefitEntity benefit;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;
}
