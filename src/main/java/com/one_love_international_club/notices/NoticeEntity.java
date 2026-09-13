package com.one_love_international_club.notices;

import com.one_love_international_club.enums.Audience;
import com.one_love_international_club.enums.NoticeType;
import com.one_love_international_club.setting.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "notices")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeEntity extends BaseEntity {

    @Column(name = "notice_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private NoticeType noticeType = NoticeType.ANNOUNCEMENT;

    @Column(name = "audience_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private Audience audience = Audience.ALL_MEMBERS;

    @Column(name = "subject", nullable = false, unique = true)
    private String subject;

    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "save_as_draft", nullable = false)
    private Boolean saveAsDraft = true;
}
