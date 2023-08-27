package com.example.hotelgfl.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RoomDto {

    @NotNull
    private Long roomNumber;

    @Min(1)
    @NotNull
    private Integer bedCount;

    @Min(0)
    @NotNull
    private Double dayPrice;

    @NotNull
    private Boolean isFree = true;

    @NotBlank
    private String roomClassName;
}
