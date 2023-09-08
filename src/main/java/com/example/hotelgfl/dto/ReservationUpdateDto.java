package com.example.hotelgfl.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReservationUpdateDto {

    @NotNull
    private LocalDateTime from;

    @NotNull
    private LocalDateTime to;

    @NotNull
    private String renterEmail;
}
