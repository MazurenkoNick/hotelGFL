package com.example.hotelgfl.repository;

import com.example.hotelgfl.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
