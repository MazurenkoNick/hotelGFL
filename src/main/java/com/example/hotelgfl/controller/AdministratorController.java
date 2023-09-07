package com.example.hotelgfl.controller;

import com.example.hotelgfl.dto.AdministratorDto;
import com.example.hotelgfl.dto.ResponseAdministratorDto;
import com.example.hotelgfl.dto.UpdateAdministratorDto;
import com.example.hotelgfl.service.AdministratorService;
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdministratorDto> delete(@PathVariable("email") String email) {
        AdministratorDto deletedDto = administratorService.delete(email);
        return ResponseEntity.ok(deletedDto);
    }

    @PutMapping("/{email}")
    @PreAuthorize("authentication.name == #email or hasRole('ADMIN')")
    public ResponseEntity<AdministratorDto> update(@PathVariable("email") String email,
                                                   @Valid @RequestBody UpdateAdministratorDto administratorDto) {
        AdministratorDto updatedDto = administratorService.update(email, administratorDto);
        return ResponseEntity.ok(updatedDto);
    }

    @GetMapping("/{email}")
    @PreAuthorize("authentication.name == #email or hasRole('ADMIN')")
    public ResponseEntity<ResponseAdministratorDto> get(@PathVariable("email") String email) {
        ResponseAdministratorDto administratorDto = administratorService.getDto(email);
        return ResponseEntity.ok(administratorDto);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ResponseAdministratorDto>> getAll() {
        List<ResponseAdministratorDto> administratorDtos = administratorService.getAll();
        return ResponseEntity.ok(administratorDtos);
    }
}
