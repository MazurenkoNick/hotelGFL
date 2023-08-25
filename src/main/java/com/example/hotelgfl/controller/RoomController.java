package com.example.hotelgfl.controller;

import com.example.hotelgfl.dto.RoomDto;
import com.example.hotelgfl.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<RoomDto> create(@Valid @RequestBody RoomDto roomDto) {
        RoomDto persistedDto = roomService.create(roomDto);
        return new ResponseEntity<>(persistedDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{roomNumber}")
    public ResponseEntity<RoomDto> remove(@PathVariable("roomNumber") Long roomNumber) {
        RoomDto removedDto = roomService.remove(roomNumber);
        return ResponseEntity.ok(removedDto);
    }

    // TODO: ADD FUNCTIONALITY TO READ & UPDATE ROOMS.
}
