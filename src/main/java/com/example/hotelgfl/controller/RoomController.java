package com.example.hotelgfl.controller;

import com.example.hotelgfl.dto.RoomDto;
import com.example.hotelgfl.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<RoomDto> create(@Valid @RequestBody RoomDto roomDto) {
        RoomDto persistedDto = roomService.create(roomDto);
        return new ResponseEntity<>(persistedDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{roomNumber}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<RoomDto> remove(@PathVariable("roomNumber") Long roomNumber) {
        RoomDto removedDto = roomService.remove(roomNumber);
        return ResponseEntity.ok(removedDto);
    }

    @PutMapping("/{roomNumber}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<RoomDto> update(@PathVariable("roomNumber") Long roomNumber,
                                          @RequestBody RoomDto roomDto) {
        RoomDto updatedDto = roomService.update(roomNumber, roomDto);
        return ResponseEntity.ok(updatedDto);
    }

    @GetMapping("/{roomNumber}")
    public ResponseEntity<RoomDto> get(@PathVariable("roomNumber") Long roomNumber) {
        RoomDto roomDto = roomService.get(roomNumber);
        return ResponseEntity.ok(roomDto);
    }

    @GetMapping
    public ResponseEntity<List<RoomDto>> getAll() {
        List<RoomDto> roomDtos = roomService.getAll();
        return ResponseEntity.ok(roomDtos);
    }
}
