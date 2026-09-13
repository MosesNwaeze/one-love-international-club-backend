package com.one_love_international_club.notices;

import com.one_love_international_club.enums.Audience;
import com.one_love_international_club.enums.NoticeType;
import com.one_love_international_club.setting.dto.BaseDto;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class NoticeDto extends BaseDto {

    @NotNull(message = "Notice type is required.")
    private NoticeType noticeType;

    @NotNull(message = "Audience type is required.")
    private Audience audience;

    @NotBlank(message = "Subject is required.")
    private String subject;

    @NotBlank(message = "Message is required.")
    private String message;

    private Boolean saveAsDraft;
}
