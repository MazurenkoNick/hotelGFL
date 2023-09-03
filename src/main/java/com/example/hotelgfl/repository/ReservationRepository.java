package com.example.hotelgfl.repository;

import com.example.hotelgfl.dto.ReservationResponseDto;
import com.example.hotelgfl.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT new com.example.hotelgfl.dto.ReservationResponseDto(" +
            "r.id, r.fromDateTime, r.toDateTime, r.room.roomNumber, r.renter.email, r.administrator.email) " +
            "FROM Reservation r")
    List<ReservationResponseDto> getAllReservationResponseDtos();

    @Query("SELECT new com.example.hotelgfl.dto.ReservationResponseDto(" +
            "r.id, r.fromDateTime, r.toDateTime, r.room.roomNumber, r.renter.email, r.administrator.email) " +
            "FROM Reservation r " +
            "WHERE r.id = :id")
    ReservationResponseDto getReservationResponseDtoById(Long id);

    @Query("SELECT new com.example.hotelgfl.dto.ReservationResponseDto(" +
            "r.id, r.fromDateTime, r.toDateTime, r.room.roomNumber, r.renter.email, r.administrator.email) " +
            "FROM Reservation r " +
            "LEFT JOIN r.receipt rec " +
            "WHERE rec IS NULL")
    List<ReservationResponseDto> getAllReservationResponseDtosNonCheckedOut();

    @Query("SELECT DISTINCT r FROM Reservation r " +
            "LEFT JOIN FETCH r.administrator " +
            "LEFT JOIN FETCH r.receipt " +
            "LEFT JOIN FETCH r.room room " +
            "LEFT JOIN FETCH room.roomClass " +
            "LEFT JOIN FETCH r.renter ren " +
            "LEFT JOIN FETCH ren.discounts disc " +
            "LEFT JOIN FETCH disc.roomClass " +
            "WHERE r.id = :id"
    )
    Optional<Reservation> findByIdFetchDiscounts(Long id);
}
