package com.one_love_international_club.dto.request;

import com.one_love_international_club.enums.Gender;
import com.one_love_international_club.enums.MaritalStatus;
import com.one_love_international_club.enums.UserType;
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

    @NotNull(message = "User type is required.")
    private UserType userType;

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

}
