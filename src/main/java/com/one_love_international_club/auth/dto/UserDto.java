package com.one_love_international_club.auth.dto;

import com.one_love_international_club.committee.CommitteeDto;
import com.one_love_international_club.enums.Gender;
import com.one_love_international_club.enums.MaritalStatus;
import com.one_love_international_club.enums.Title;
import com.one_love_international_club.setting.dto.BaseDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.one_love_international_club.setting.dto.Status;
import com.one_love_international_club.setting.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto extends BaseDto {
    private String firstName;
    private String lastName;
    private String otherName;
    private Role role;
    private Title title;
    private String currentAddress;
    private String permanentAddress;
    private LocalDate dateOfBirth;
    private Gender gender;
    private MaritalStatus maritalStatus;
    private String occupation;
    private Status status = Status.PENDING;
    private String email;
    private String phoneNumber;
    private String profilePic;
    private String picPublicId;
    private Boolean isRootAdmin;
    private UserDto guarantor;
    private String letterOfUndertaking;
    private String letterOfUndertakingPublicId;
    @JsonIgnore
    private CommitteeDto committee;
    private String BankAccountName;
    private String BankAccountNumber;
    private String registrationFeeUrl;
    private String registrationFeePublicId;
    private String registrationFormUrl;
    private String registrationFormPublic;
}
