package com.example.hotelgfl.dto;

import com.example.hotelgfl.model.Rank;

public record ResponseAdministratorDto(Rank rank, double salary, String firstName, String lastName,
                                       String email, String passportId, String phoneNumber) {}
