package com.example.hotelgfl.repository;

import com.example.hotelgfl.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByRoomNumber(Long roomNumber);
}
