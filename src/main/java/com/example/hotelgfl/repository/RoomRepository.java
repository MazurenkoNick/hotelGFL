package com.example.hotelgfl.repository;

import com.example.hotelgfl.dto.RoomDto;
import com.example.hotelgfl.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByRoomNumber(Long roomNumber);

    @Query("SELECT new com.example.hotelgfl.dto.RoomDto(" +
            "r.roomNumber, r.bedCount, r.dayPrice, r.roomClass.name) " +
            "FROM Room r " +
            "WHERE r.roomNumber = :roomNumber")
    Optional<RoomDto> findRoomDtoByNumber(Long roomNumber);

    @Query("SELECT new com.example.hotelgfl.dto.RoomDto(" +
            "r.roomNumber, r.bedCount, r.dayPrice, r.roomClass.name) " +
            "FROM Room r")
    List<RoomDto> findAllRoomDtos();

    @Query("SELECT new com.example.hotelgfl.dto.RoomDto(" +
            "r.roomNumber, r.bedCount, r.dayPrice, r.roomClass.name) " +
            "FROM Room r " +
            "LEFT JOIN r.reservations res " +
            "WHERE NOT (DATE(res.fromDateTime) <= CURRENT DATE AND DATE(res.toDateTime) >= CURRENT DATE) OR res IS NULL"
    )
    List<RoomDto> findAllFreeRoomDtos();

    @Query("SELECT DISTINCT NEW com.example.hotelgfl.dto.RoomDto(r.roomNumber, r.bedCount, r.dayPrice, r.roomClass.name) " +
            "FROM Room r " +
            "LEFT JOIN r.reservations res " +
            "WHERE NOT (DATE(res.fromDateTime) <= :to AND DATE(res.toDateTime) >= :from) OR res IS NULL"
    )
    List<RoomDto> findAllFreeRoomDtos(LocalDate from, LocalDate to);
}
