package com.example.hotelgfl.repository;

import com.example.hotelgfl.model.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
}
