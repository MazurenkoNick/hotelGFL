package com.example.hotelgfl.dto.administrator;

import com.example.hotelgfl.model.Rank;

public record AdministratorResponseDto(Rank rank, double salary, String firstName, String lastName,
                                       String email, String passportId, String phoneNumber) {}
