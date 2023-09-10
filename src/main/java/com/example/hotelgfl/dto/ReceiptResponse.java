package com.example.hotelgfl.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
@Setter
public class ReceiptResponse {

    private Long id;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private Long reservationId;
    private double totalPrice;
    private String renterEmail;
}
