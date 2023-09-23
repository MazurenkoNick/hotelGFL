package com.example.hotelgfl.repository;

import com.example.hotelgfl.dto.room.RoomDto;
import com.example.hotelgfl.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByRoomNumber(Long roomNumber);

    @Query("SELECT new com.example.hotelgfl.dto.room.RoomDto(" +
            "r.roomNumber, r.bedCount, r.dayPrice, r.roomClass.name) " +
            "FROM Room r " +
            "WHERE r.roomNumber = :roomNumber")
    Optional<RoomDto> findRoomDtoByNumber(Long roomNumber);

    @Query("SELECT new com.example.hotelgfl.dto.room.RoomDto(" +
            "r.roomNumber, r.bedCount, r.dayPrice, r.roomClass.name) " +
            "FROM Room r")
    List<RoomDto> findAllRoomDtos();

    @Query("SELECT new com.example.hotelgfl.dto.room.RoomDto(" +
            "r.roomNumber, r.bedCount, r.dayPrice, r.roomClass.name) " +
            "FROM Room r " +
            "WHERE NOT EXISTS (" +
            "    SELECT 1 " +
            "    FROM Reservation res " +
            "    WHERE res.room = r " +
            "    AND (DATE(res.fromDateTime) <= CURRENT DATE AND DATE(res.toDateTime) >= CURRENT DATE))"
    )
    List<RoomDto> findAllFreeRoomDtos();

    @Query("SELECT new com.example.hotelgfl.dto.room.RoomDto(" +
            "r.roomNumber, r.bedCount, r.dayPrice, r.roomClass.name) " +
            "FROM Room r " +
            "WHERE NOT EXISTS (" +
            "    SELECT 1 " +
            "    FROM Reservation res " +
            "    WHERE res.room = r " +
            "    AND (DATE(res.fromDateTime) <= :to AND DATE(res.toDateTime) >= :from))"
    )
    List<RoomDto> findAllFreeRoomDtos(LocalDate from, LocalDate to);

    @Query("SELECT new com.example.hotelgfl.dto.room.RoomDto(" +
            "r.roomNumber, r.bedCount, r.dayPrice, r.roomClass.name) " +
            "FROM Room r " +
            "WHERE r.roomNumber = :roomNumber " +
            "AND NOT EXISTS (" +
            "    SELECT 1 " +
            "    FROM Reservation res " +
            "    WHERE res.room = r " +
            "    AND (DATE(res.fromDateTime) <= :to AND DATE(res.toDateTime) >= :from))")
    Optional<RoomDto> findRoomIfFree(Long roomNumber, LocalDate from, LocalDate to);

    @Query("SELECT new com.example.hotelgfl.dto.room.RoomDto(" +
            "r.roomNumber, r.bedCount, r.dayPrice, r.roomClass.name) " +
            "FROM Room r " +
            "WHERE r.roomNumber = :roomNumber " +
            "AND NOT EXISTS (" +
            "    SELECT 1 " +
            "    FROM Reservation res " +
            "    WHERE res.room = r " +
            "    AND res.id != :reservationId " +
            "    AND (DATE(res.fromDateTime) <= :to AND DATE(res.toDateTime) >= :from))")
    Optional<RoomDto> findRoomIfFreeUpdate(Long reservationId, Long roomNumber, LocalDate from, LocalDate to);
}
