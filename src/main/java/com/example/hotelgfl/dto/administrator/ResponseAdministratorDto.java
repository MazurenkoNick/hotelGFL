package com.example.hotelgfl.dto.administrator;

import com.example.hotelgfl.model.Rank;

public record ResponseAdministratorDto(Rank rank, double salary, String firstName, String lastName,
                                       String email, String passportId, String phoneNumber) {}
