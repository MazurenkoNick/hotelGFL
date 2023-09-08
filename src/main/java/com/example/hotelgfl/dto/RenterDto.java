package com.example.hotelgfl.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RenterDto {

    @Pattern(regexp = "[A-Z][a-z]+(?:\\s[A-Z][a-z]+)*",
            message = "Must start with a capital letter followed by one or more lowercase letters")
    @NotBlank
    private String firstName;

    @Pattern(regexp = "[A-Z][a-z]+(?:\\s[A-Z][a-z]+)*",
            message = "Must start with a capital letter followed by one or more lowercase letters")
    @NotBlank
    private String lastName;

    @NotBlank
    private String passportId;

    @NotBlank
    private String phoneNumber;

    @Email
    @NotBlank
    private String email;
}
