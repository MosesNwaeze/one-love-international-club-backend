package com.one_love_international_club.minutes;

import com.one_love_international_club.meeting.MeetingDto;
import com.one_love_international_club.setting.dto.BaseDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MinutesDto extends BaseDto {

    @NotNull(message = "Meeting is required.")
    private MeetingDto meeting;

    @NotBlank(message = "Discussion summary is required.")
    private String discussionSummary;

    @NotBlank(message = "Resolution is required.")
    private String resolution;

    private Boolean draft;
}
