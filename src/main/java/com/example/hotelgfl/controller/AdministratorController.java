package com.example.hotelgfl.controller;

import com.example.hotelgfl.dto.administrator.AdministratorDto;
import com.example.hotelgfl.dto.administrator.AdministratorResponseDto;
import com.example.hotelgfl.dto.administrator.AdministratorUpdateDto;
import com.example.hotelgfl.service.AdministratorService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/administrator")
@RequiredArgsConstructor
public class AdministratorController {

    private final AdministratorService administratorService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdministratorDto> create(@Valid @RequestBody AdministratorDto administratorDto) {
        AdministratorDto persistedDto = administratorService.create(administratorDto);
        return new ResponseEntity<>(persistedDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{email}")
    @PreAuthorize("authentication.name == #email or hasRole('ADMIN')")
    public ResponseEntity<AdministratorDto> delete(HttpSession session, @PathVariable("email") String email) {
        AdministratorDto deletedDto = administratorService.delete(session, email);
        return ResponseEntity.ok(deletedDto);
    }

    @PutMapping("/{email}")
    @PreAuthorize("authentication.name == #email or hasRole('ADMIN')")
    public ResponseEntity<AdministratorDto> update(@PathVariable("email") String email,
                                                   @Valid @RequestBody AdministratorUpdateDto administratorDto) {
        AdministratorDto updatedDto = administratorService.update(email, administratorDto);
        return ResponseEntity.ok(updatedDto);
    }

    @GetMapping("/{email}")
    public ResponseEntity<AdministratorResponseDto> get(@PathVariable("email") String email) {
        AdministratorResponseDto administratorDto = administratorService.get(email, AdministratorResponseDto.class);
        return ResponseEntity.ok(administratorDto);
    }

    @GetMapping
    public ResponseEntity<List<AdministratorResponseDto>> getAll() {
        List<AdministratorResponseDto> administratorDtos = administratorService.getAll(AdministratorResponseDto.class);
        return ResponseEntity.ok(administratorDtos);
    }
}
