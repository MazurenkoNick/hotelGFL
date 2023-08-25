package com.example.hotelgfl.repository;

import com.example.hotelgfl.dto.RoomDto;
import com.example.hotelgfl.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByRoomNumber(Long roomNumber);

    @Query("SELECT new com.example.hotelgfl.dto.RoomDto(" +
            "r.roomNumber, r.bedCount, r.dayPrice, r.isFree, r.roomClass.name) " +
            "FROM Room r " +
            "WHERE r.roomNumber = :roomNumber")
    Optional<RoomDto> findRoomDtoByNumber(Long roomNumber);

    @Query("SELECT new com.example.hotelgfl.dto.RoomDto(" +
            "r.roomNumber, r.bedCount, r.dayPrice, r.isFree, r.roomClass.name) " +
            "FROM Room r")
    List<RoomDto> findAllRoomDtos();
}
