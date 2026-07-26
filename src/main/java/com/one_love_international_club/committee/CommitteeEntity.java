package com.one_love_international_club.committee;

import com.one_love_international_club.auth.entity.UserEntity;
import com.one_love_international_club.setting.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "committees")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommitteeEntity extends BaseEntity {

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description", nullable = false, unique = true, columnDefinition = "TEXT")
    private String description;

    @Column(name = "total_members_allowed", nullable = false)
    private Integer totalMembersAllowed;

    @Column(name = "resolution_report")
    private String resolutionReport;

    @Column(name = "amount_received")
    private BigDecimal amountReceived;

    @Column(name = "amount_spent")
    private BigDecimal amountSpent;

    @OneToMany(mappedBy = "committee")
    private Set<UserEntity> member = new HashSet<>();

}
