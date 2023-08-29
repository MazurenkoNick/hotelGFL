package com.example.hotelgfl.controller;

import com.example.hotelgfl.dto.ReservationDto;
import com.example.hotelgfl.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationDto> create(@Valid @RequestBody ReservationDto reservationDto) {
        ReservationDto responseDto = reservationService.create(reservationDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
}
