package com.example.hotelgfl.dto;

import com.example.hotelgfl.model.Rank;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class UpdateAdministratorDto {

    @NotNull
    private Rank rank;

    @Min(0)
    @NotNull
    private Double salary;

    @Pattern(message = "Must start with a capital letter followed by one or more lowercase letters",
            regexp = "[A-Z][a-z]+(?:\\s[A-Z][a-z]+)*")
    @NotBlank
    private String firstName;

    @Pattern(message = "Must start with a capital letter followed by one or more lowercase letters",
            regexp = "[A-Z][a-z]+(?:\\s[A-Z][a-z]+)*")
    @NotBlank
    private String lastName;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String passportId;

    @NotBlank
    private String phoneNumber;
}
