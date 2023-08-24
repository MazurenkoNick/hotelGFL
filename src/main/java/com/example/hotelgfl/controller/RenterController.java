package com.example.hotelgfl.controller;

import com.example.hotelgfl.dto.RenterDto;
import com.example.hotelgfl.service.RenterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
