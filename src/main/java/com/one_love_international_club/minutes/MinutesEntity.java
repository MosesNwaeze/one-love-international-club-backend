package com.one_love_international_club.minutes;

import com.one_love_international_club.meeting.MeetingEntity;
import com.one_love_international_club.setting.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "minutes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MinutesEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "meeting_id",
            nullable = false, columnDefinition = "UUID")
    @JdbcTypeCode(SqlTypes.UUID)
    private MeetingEntity meeting;

    @Column(
            name = "discussion_summary",
            nullable = false, columnDefinition = "TEXT")
    private String discussionSummary;

    @Column(
            name = "resolution",
            nullable = false, columnDefinition = "TEXT")
    private String resolution;

    @Column(name = "save_as_draft", nullable = false)
    private Boolean draft = true;
}
