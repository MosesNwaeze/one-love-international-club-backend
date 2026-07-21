package com.one_love_international_club.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.one_love_international_club.dto.Status;
import com.one_love_international_club.enums.Gender;
import com.one_love_international_club.enums.MaritalStatus;
import com.one_love_international_club.enums.UserType;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRegisterDto {
    private UUID id;

    private String firstName;

    private String lastName;

    private String otherName;

    private UserType userType;

    private String title;

    private String currentAddress;

    private String permanentAddress;

    private LocalDate dateOfBirth;

    private Gender gender;

    private MaritalStatus maritalStatus;

    private String occupation;

    private Status status;

    private String email;

    @JsonIgnore
    private String password;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean isRootAdmin;

    private String profilePic;

    private String picPublicId;

}
