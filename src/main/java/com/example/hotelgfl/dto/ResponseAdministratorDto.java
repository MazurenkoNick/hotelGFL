package com.example.hotelgfl.dto;

import com.example.hotelgfl.model.Rank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResponseAdministratorDto {

    private Rank rank;
    private double salary;
    private String firstName;
    private String lastName;
    private String email;
    private String passportId;
    private String phoneNumber;
}

