package com.example.hotelgfl.controller;

import com.example.hotelgfl.dto.ReceiptResponse;
import com.example.hotelgfl.dto.ReservationDto;
import com.example.hotelgfl.dto.ReservationResponseDto;
import com.example.hotelgfl.dto.ReservationUpdateDto;
import com.example.hotelgfl.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponseDto> create(@Valid @RequestBody ReservationDto reservationDto) {
        ReservationResponseDto responseDto = reservationService.create(reservationDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationResponseDto> update(@PathVariable("id") Long id,
                                                         @Valid @RequestBody ReservationUpdateDto reservationDto) {
        ReservationResponseDto responseDto = reservationService.update(id, reservationDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ReservationResponseDto> delete(@PathVariable("id") Long id) {
        ReservationResponseDto responseDto = reservationService.remove(id);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDto> get(@PathVariable("id") Long id) {
        ReservationResponseDto responseDto = reservationService.get(id);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponseDto>> getAll() {
        List<ReservationResponseDto> responseDtos = reservationService.getAll();
        return ResponseEntity.ok(responseDtos);
    }

    @PostMapping("{id}/checkout")
    public ResponseEntity<ReceiptResponse> checkout(@PathVariable("id") Long id,
                                                           @RequestParam(required = false) LocalDateTime checkoutDateTime) {
        ReceiptResponse receipt = reservationService.checkout(id, checkoutDateTime);
        return ResponseEntity.ok(receipt);
    }
}
