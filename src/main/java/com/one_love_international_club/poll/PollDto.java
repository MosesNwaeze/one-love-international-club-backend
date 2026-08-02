package com.one_love_international_club.poll;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.one_love_international_club.auth.dto.UserDto;
import com.one_love_international_club.auth.entity.UserEntity;
import com.one_love_international_club.setting.dto.BaseDto;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PollDto extends BaseDto {
    @NotBlank(message = "Question is required.")
    private String question;

    @NotNull(message = "Options are required")
    private List<String> options;

    private Map<String, Integer> votes;

    @NotNull(message = "Close date is required.")
    private LocalDateTime closeDate;

    private UserDto createdBy;

    private Set<UUID> votedBy;
}
