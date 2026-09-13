package com.one_love_international_club.meeting;

import com.one_love_international_club.setting.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "meetings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeetingEntity extends BaseEntity {

    @Column(name = "meeting_title", nullable = false, unique = true)
    private String meetingTitle;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "agender", nullable = false)
    private String agender;

    @Column(name = "venue", nullable = false)
    private String venue;

    @Column(name = "documents")
    private String documents;

    @Column(name = "document_public_id")
    private String documentPublicId;

    @Column(name = "save_as_draft", nullable = false)
    private Boolean saveAsDraft = true;

}
