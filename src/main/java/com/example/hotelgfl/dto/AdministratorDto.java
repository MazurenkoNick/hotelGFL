package com.example.hotelgfl.dto;

import com.example.hotelgfl.model.Rank;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AdministratorDto {

    @NotNull
    private Rank rank;

    @Min(0)
    private double salary;

    @Pattern(message = "Must be minimum 6 characters, at least one letter and one number",
            regexp = "(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}")
    private String password;

    @Pattern(message = "Must start with a capital letter followed by one or more lowercase letters",
            regexp = "[A-Z][a-z]+")
    private String firstName;

    @Pattern(message = "Must start with a capital letter followed by one or more lowercase letters",
            regexp = "[A-Z][a-z]+")
    private String lastName;

    @Email
    private String email;

    @NotBlank
    private String passportId;

    @NotBlank
    private String phoneNumber;
}
