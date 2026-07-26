package com.one_love_international_club.resignation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.one_love_international_club.auth.dto.UserDto;
import com.one_love_international_club.benefits.BenefitDto;
import com.one_love_international_club.enums.ResignationStatus;
import com.one_love_international_club.enums.ResignationType;
import com.one_love_international_club.setting.dto.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResignationDto extends BaseDto {

    private UserDto user;

    private String resignationLetter;

    private String resignationLetterPublicId;

    private String reasonOfResignation;

    private ResignationStatus resignationStatus;

    @NotNull(message = "Resignation type is required")
    @Schema(name = "Resignation Type", example = "SUSPEND")
    private ResignationType resignationType;

    private BenefitDto benefit;
}
