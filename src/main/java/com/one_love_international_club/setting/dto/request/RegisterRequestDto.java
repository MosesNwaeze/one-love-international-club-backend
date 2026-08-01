package com.one_love_international_club.setting.dto.request;

import com.one_love_international_club.auth.dto.UserDto;
import com.one_love_international_club.enums.Gender;
import com.one_love_international_club.enums.MaritalStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDto {

    @NotBlank(message = "First name is required.")
    private String firstName;

    @NotBlank(message = "Last name is required.")
    private String lastName;

    private String otherName;

    private String title;

    @NotBlank(message = "Current address is required.")
    private String currentAddress;

    private String permanentAddress;

    @NotNull(message = "Date of birth is required.")
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender is required.")
    private Gender gender;

    @NotNull(message = "Marital status is required.")
    private MaritalStatus maritalStatus;

    @NotBlank(message = "Occupation is required.")
    private String occupation;

    @NotBlank(message = "Email is required.")
    private String email;

    @NotBlank(message = "Password is required.")
    private String password;

    @NotBlank(message = "Phone number is required.")
    private String phoneNumber;

    private String profilePic;

    private Boolean isRootAdmin;

    private UserDto guarantor;

    private String letterOfUndertaking;

    private String bankAccountName;

    private String bankAccountNumber;

    private String registrationFeeUrl;

    private String registrationFormUrl;

}
