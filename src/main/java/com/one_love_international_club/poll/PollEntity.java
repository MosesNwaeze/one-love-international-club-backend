package com.one_love_international_club.poll;

import com.one_love_international_club.auth.entity.UserEntity;
import com.one_love_international_club.setting.entity.BaseEntity;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "polls")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PollEntity extends BaseEntity {

    @Column(name = "question", nullable = false, unique = true)
    private String question;

    @Column(name = "options", columnDefinition = "JSONB", nullable = false)
    @Type(value = JsonBinaryType.class)
    private List<String> options;

    @Column(name = "votes", columnDefinition = "JSONB")
    @Type(value = JsonBinaryType.class)
    private Map<String, Integer> votes = new HashMap<>();

    @Column(name = "close_date", nullable = false)
    private LocalDateTime closeDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JdbcTypeCode(SqlTypes.UUID)
    @JoinColumn(name = "created_by", columnDefinition = "UUID", nullable = false)
    private UserEntity createdBy;

    @Type(JsonBinaryType.class)
    @Column(name = "voted_by", columnDefinition = "JSONB")
    private List<UUID> votedBy = new ArrayList<>();
}
