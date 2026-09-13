package com.one_love_international_club.meeting;

import com.fasterxml.jackson.annotation.JsonView;
import com.one_love_international_club.setting.dto.BaseDto;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetingDto extends BaseDto {

    @NotBlank(message = "Meeting title is required.")
    private String meetingTitle;

    @NotNull(message = "Meeting date is required.")
    private LocalDateTime date;

    @NotBlank(message = "Agender is required.")
    private String agender;

    @NotBlank(message = "Venue is required.")
    private String venue;

    private String documents;

    private String documentPublicId;

    private Boolean saveAsDraft = true;
}
