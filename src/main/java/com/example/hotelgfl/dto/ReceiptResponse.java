package com.example.hotelgfl.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReceiptResponse {

    private Long id;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private Long reservationId;
    private double totalPrice;
}
