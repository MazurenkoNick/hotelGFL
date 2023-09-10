package com.example.hotelgfl.dto;

import com.example.hotelgfl.model.Rank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
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

