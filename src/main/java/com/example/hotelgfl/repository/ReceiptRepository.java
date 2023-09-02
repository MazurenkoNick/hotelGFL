package com.example.hotelgfl.repository;

import com.example.hotelgfl.dto.ReceiptResponse;
import com.example.hotelgfl.model.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

    @Query("SELECT new com.example.hotelgfl.dto.ReceiptResponse(" +
            "r.id, r.checkIn, r.checkOut, r.reservation.id, r.totalPrice, r.reservation.renter.email) " +
            "FROM Receipt r " +
            "WHERE r.id = :id")
    ReceiptResponse findReceiptResponseById(Long id);
}
