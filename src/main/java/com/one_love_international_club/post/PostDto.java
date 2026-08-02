package com.one_love_international_club.post;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.one_love_international_club.auth.dto.UserDto;
import com.one_love_international_club.enums.PostType;
import com.one_love_international_club.setting.dto.BaseDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostDto extends BaseDto {

    @NotBlank(message = "Title is required.")
    private String title;

    @NotNull(message = "Post type is required.")
    private PostType postType;

    @NotBlank(message = "Content is required.")
    private String content;

    private String postImage;

    private String postPublicId;

    private Long totalViewed;

    private UserDto createdBy;
}
