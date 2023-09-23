package com.example.hotelgfl.dto;

import java.time.LocalDateTime;

public record ReceiptResponse(Long id, LocalDateTime checkIn, LocalDateTime checkOut, Long reservationId,
                              double totalPrice, String reservationRenterEmail) {

}
