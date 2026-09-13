package com.one_love_international_club.file_upload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.one_love_international_club.setting.dto.BaseDto;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UploadDto extends BaseDto {

    @NotBlank(
            message = "File is in the format base64encodedUrl string and it is required.")
    private String file;

    @NotBlank(message = "Category is required.")
    private String category;

    private UploadedBy uploadedBy;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UploadedBy {
        private String firstName;
        private String lastName;
    }
}
