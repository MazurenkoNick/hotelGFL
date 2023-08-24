package com.example.hotelgfl.controller;

import com.example.hotelgfl.dto.AdministratorDto;
import com.example.hotelgfl.service.AdministratorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/administrator")
@RequiredArgsConstructor
public class AdministratorController {

    private final AdministratorService administratorService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<AdministratorDto> create(@Valid @RequestBody AdministratorDto administratorDto) {
        AdministratorDto persistedDto = administratorService.create(administratorDto);
        return new ResponseEntity<>(persistedDto, HttpStatus.CREATED);
    }

    @DeleteMapping
    @RequestMapping("/{email}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<AdministratorDto> delete(@PathVariable("email") String email) {
        AdministratorDto deletedDto = administratorService.delete(email);
        return ResponseEntity.ok(deletedDto);
    }
}
