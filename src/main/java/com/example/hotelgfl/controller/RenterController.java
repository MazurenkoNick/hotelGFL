package com.example.hotelgfl.controller;

import com.example.hotelgfl.dto.RenterDto;
import com.example.hotelgfl.dto.RenterResponseDto;
import com.example.hotelgfl.service.RenterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/renter")
@RequiredArgsConstructor
public class RenterController {

    private final RenterService renterService;

    @PostMapping
    public ResponseEntity<RenterDto> create(@Valid @RequestBody RenterDto renterDto) {
        RenterDto persisted = renterService.create(renterDto);
        return new ResponseEntity<>(persisted, HttpStatus.CREATED);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<RenterDto> remove(@PathVariable("email") String email) {
        RenterDto removed = renterService.remove(email);
        return ResponseEntity.ok(removed);
    }

    @PutMapping("/{email}")
    public ResponseEntity<RenterDto> update(@PathVariable("email") String email,
                                            @Valid @RequestBody RenterDto renterDto) {
        RenterDto updatedDto = renterService.update(email, renterDto);
        return ResponseEntity.ok(updatedDto);
    }

    @GetMapping("/{email}")
    public ResponseEntity<RenterResponseDto> get(@PathVariable("email") String email) {
        RenterResponseDto renterDto = renterService.get(email, RenterResponseDto.class);
        return ResponseEntity.ok(renterDto);
    }

    @GetMapping
    public ResponseEntity<List<RenterResponseDto>> getAll() {
        List<RenterResponseDto> renterDtos = renterService.getAll(RenterResponseDto.class);
        return ResponseEntity.ok(renterDtos);
    }

    // TODO: DISCOUNTS
}
