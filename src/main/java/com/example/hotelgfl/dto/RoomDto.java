package com.example.hotelgfl.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RoomDto {

    @NotNull
    private Long roomNumber;

    @Min(1)
    private int bedCount;

    @Min(0)
    private double dayPrice;

    @NotNull
    private Boolean isFree = true;

    @NotBlank
    private String roomClassName;
}
